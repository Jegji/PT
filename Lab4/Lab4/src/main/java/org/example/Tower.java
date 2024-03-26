package org.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "Tower")
public class Tower {
    @Id
    private String name;
    private int height;

    @OneToMany(
            mappedBy = "tower",
            cascade = CascadeType.ALL
    )
    private List<Mage> mages = new ArrayList<>();

    public Tower() {
    }

    public Tower(String name, int height) {
        this.name = name;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void setMages(List<Mage> mages) {
        this.mages = mages;
    }

    // W klasie Tower
    @Override
    public String toString() {
        StringBuilder magesNames = new StringBuilder();
        for (Mage mage : mages) {
            magesNames.append(mage.getName()).append(" ");
        }

        return "name = " + name + ", height = " + height + ", mages = " + magesNames.toString();
    }
}