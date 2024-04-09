package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MageRepositoryTest {

    @Test
    public void testFindExistingMage() {
        Mage mage = new Mage("Merlin", 10);
        MageRepository repository = new MageRepository();
        repository.save(mage);

        assertTrue(repository.find("Merlin").isPresent());
    }

    @Test
    public void testFindNonExistingMage() {
        MageRepository repository = new MageRepository();
        assertFalse(repository.find("Gandalf").isPresent());
    }

    @Test
    public void testDeleteExistingMage() {
        Mage mage = new Mage("Gandalf", 15);
        MageRepository repository = new MageRepository();
        repository.save(mage);

        repository.delete("Gandalf");
        assertFalse(repository.find("Gandalf").isPresent());
    }

    @Test
    public void testDeleteNonExistingMage() {
        MageRepository repository = new MageRepository();
        assertThrows(IllegalArgumentException.class, () -> repository.delete("Merlin"));
    }

    @Test
    public void testSaveNewMage() {
        Mage mage = new Mage("Dumbledore", 20);
        MageRepository repository = new MageRepository();
        repository.save(mage);

        assertTrue(repository.find("Dumbledore").isPresent());
    }

    @Test
    public void testSaveExistingMage() {
        Mage mage = new Mage("Gandalf", 15);
        MageRepository repository = new MageRepository();
        repository.save(mage);

        assertThrows(IllegalArgumentException.class, () -> repository.save(mage));
    }
}
