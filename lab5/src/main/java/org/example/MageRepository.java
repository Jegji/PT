package org.example;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class MageRepository {
    private Collection<Mage> collection;
    public MageRepository(){
        collection = new HashSet<>();
    }
    public Optional<Mage> find(String name) {
        Mage mage = collection.stream()
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(mage);
    }

    public void delete(String name) {
        boolean removed = collection.removeIf(m -> m.getName().equals(name));
        if (!removed) {
            throw new IllegalArgumentException("Mage with name " + name + " not found for deletion");
        }
    }

    public void save(Mage mage) {
        boolean exists = collection.stream().anyMatch(m -> m.getName().equals(mage.getName()));
        if (exists) {
            throw new IllegalArgumentException("Mage with name " + mage.getName() + " already exists");
        }
        collection.add(mage);
    }
}
