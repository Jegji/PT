package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

import java.util.Optional;

class MageControllerTest {

    @Test
    public void testFindExistingMage() {
        MageRepository mockRepository = Mockito.mock(MageRepository.class);
        Mockito.when(mockRepository.find("Merlin")).thenReturn(Optional.of(new Mage("Merlin", 10)));

        MageController controller = new MageController(mockRepository);
        assertEquals("Found mage: Merlin, Level: 10", controller.find("Merlin"));
    }

    @Test
    public void testFindNonExistingMage() {
        MageRepository mockRepository = Mockito.mock(MageRepository.class);
        Mockito.when(mockRepository.find("Gandalf")).thenReturn(Optional.empty());

        MageController controller = new MageController(mockRepository);
        assertEquals("Mage not found", controller.find("Gandalf"));
    }

    @Test
    public void testDeleteExistingMage() {
        MageRepository mockRepository = Mockito.mock(MageRepository.class);
        Mockito.doNothing().when(mockRepository).delete("Gandalf");

        MageController controller = new MageController(mockRepository);
        assertEquals("done", controller.delete("Gandalf"));
    }

    @Test
    public void testDeleteNonExistingMage() {
        MageRepository mockRepository = Mockito.mock(MageRepository.class);
        Mockito.doThrow(IllegalArgumentException.class).when(mockRepository).delete("Merlin");

        MageController controller = new MageController(mockRepository);
        assertEquals("not found", controller.delete("Merlin"));
    }

    @Test
    public void testSaveNewMage() {
        MageRepository mockRepository = Mockito.mock(MageRepository.class);
        Mockito.doNothing().when(mockRepository).save(Mockito.any());

        MageController controller = new MageController(mockRepository);
        assertEquals("done", controller.save("Dumbledore", 20));
    }

    @Test
    public void testSaveExistingMage() {
        MageRepository mockRepository = Mockito.mock(MageRepository.class);
        Mockito.doThrow(IllegalArgumentException.class).when(mockRepository).save(Mockito.any());

        MageController controller = new MageController(mockRepository);
        assertEquals("bad request", controller.save("Gandalf", 15));
    }
}