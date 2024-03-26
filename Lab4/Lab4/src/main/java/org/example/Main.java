package org.example;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        scanner = new Scanner(System.in);

        addTestData();
        displayTowers();
        displayMages();
        readRemoveTower();
        readRemoveMage();
        displayTowers();
        displayMages();

        entityManagerFactory.close();
    }
    public static void addTestData(){
        addTower("AAA",100);
        addTower("BBB",150);
        addMage("aaa",10,"AAA");
        addMage("bbb",15,"BBB");
        addMage("ccc",20,"AAA");
        addMage("ddd",25,"AAA");
    }
    public static void inputMage(){
        System.out.print("Enter mage name: ");
        String name = scanner.nextLine();
        System.out.print("Enter mage level: ");
        int level = scanner.nextInt();
        scanner.nextLine();
        displayTowers();
        System.out.print("Enter tower name: ");
        String tower = scanner.nextLine();
        addMage(name,level,tower);
    }
    public static void inputTower(){
        System.out.print("Enter tower name: ");
        String name = scanner.nextLine();
        System.out.print("Enter tower heigh: ");
        int heigh = scanner.nextInt();
        scanner.nextLine();
        addTower(name,heigh);
    }
    public static void addTower(String towerName, int towerHeight) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            Tower tower = new Tower(towerName, towerHeight);
            entityManager.persist(tower);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
    public static void addMage(String mageName, int mageLevel, String towerName) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            Tower tower = entityManager.find(Tower.class, towerName);
            if (tower != null) {
                Mage mage = new Mage(mageName, mageLevel, tower);
                entityManager.persist(mage);
                entityManager.getTransaction().commit();
            } else {
                System.out.println("Tower with name " + towerName + " does not exist.");
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
    public static void displayTowers() {
        entityManager = entityManagerFactory.createEntityManager();
        List<Tower> towers = entityManager.createQuery("SELECT t FROM Tower t", Tower.class).getResultList();
        System.out.println("Towers: ");
        for (Tower tower : towers) {
            System.out.println(tower);
        }
        entityManager.close();
    }
    public static void displayMages(){
        displayMagesWithLevel(0);
    }
    public static void displayMagesWithLevel(int level){
        entityManager = entityManagerFactory.createEntityManager();
        List<Mage> mages = entityManager.createQuery("SELECT m FROM Mage m WHERE m.level > :level", Mage.class)
                .setParameter("level", level)
                .getResultList();
        System.out.println("Mages: ");
        for (Mage mage : mages) {
            System.out.println(mage);
        }
        entityManager.close();
    }
    public static void readRemoveTower(){
        System.out.print("Enter tower name to remove: ");
        String name = scanner.nextLine();
        removeTower(name);
    }
    public static void readRemoveMage(){
        System.out.print("Enter mage name to remove: ");
        String name = scanner.nextLine();
        removeMage(name);
    }
    public static void removeTower(String towerName) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            Tower towerToRemove = entityManager.find(Tower.class, towerName);
            entityManager.remove(towerToRemove);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }displayTowers();
    }
    public static void removeMage(String mageName) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            Mage mageToRemove = entityManager.find(Mage.class, mageName);
            entityManager.remove(mageToRemove);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        displayMages();
    }
}
