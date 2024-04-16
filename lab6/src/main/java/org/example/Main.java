package org.example;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        for(int i=2;i<=10;i++){
            long time = System.currentTimeMillis();
            ForkJoinPool threadPool = new ForkJoinPool(i);
            String InputPath = "C:\\Users\\krysi\\Desktop\\lab6\\Obrazy cw 6 Java\\Obrazy cw 6 Java";
            String OutputPath = "C:\\Users\\krysi\\Desktop\\lab6\\output";
            List<Path> files = null;
            Path source = Path.of(InputPath);
            Path output = Path.of(OutputPath);
            try (Stream<Path> stream = Files.list(source)){
                files = stream.collect(Collectors.toList());
            } catch (IOException e) { }

            try {
                List<Path> finalFiles = files;
                threadPool.submit(() -> finalFiles.parallelStream().map(Main::loadImp).map(Main::modify).forEach(pair -> saveImg(pair,output))).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Threads: " + i + " | time: " + (System.currentTimeMillis() - time) / 1000.0);
        }
    }

    public static Pair<String,BufferedImage> loadImp(Path path){
        try {
            BufferedImage img = ImageIO.read(path.toFile());
            String name = String.valueOf(path.getFileName());
            return Pair.of(name,img);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Pair<String, BufferedImage> modify(Pair<String,BufferedImage> pair){
        BufferedImage img = pair.getRight();
        BufferedImage output = new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int rgb = img.getRGB(i, j);
                Color color = new Color(rgb);
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                Color outColor = new Color(red, blue, green);
                int outRgb = outColor.getRGB();
                output.setRGB(i, j, outRgb);
            }
        }
        return Pair.of(pair.getLeft(),output);
    }
    public static void saveImg (Pair<String,BufferedImage> pair,Path path){
        try {
            ImageIO.write(pair.getRight(),"jpg",path.resolve(pair.getLeft()).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}