using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;

namespace ConsoleApp
{
    class Program
    {
        static void Main(string[] args)
        {
            string directoryPath = "C:\\Users\\krysi\\Desktop\\New folder (3)";
            DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);

            // Wyświetlanie zawartości katalogu
            Console.WriteLine($"Zawartość katalogu {directoryInfo.FullName}:");
            DisplayDirectoryContents(directoryInfo, 0);

            // Serializacja kolekcji
            var collection = LoadDirectoryContents(directoryInfo);
            SerializeCollection(collection);
            
            // Data ostatniej modyfikacji
            DateTime oldestDate = directoryInfo.GetOldestItemDate();
            Console.WriteLine($"Najstarsza data modyfikacji w katalogu: {oldestDate}");

            // Deserializacja kolekcji
            var deserializedCollection = DeserializeCollection();
            Console.WriteLine("\nDeserializowana kolekcja:");
            foreach (var item in deserializedCollection)
            {
                Console.WriteLine($"{item.Key} -> {item.Value}");
            }
        }

        static void DisplayDirectoryContents(DirectoryInfo directory, int indentLevel)
        {
            foreach (var file in directory.GetFiles())
            {
                Console.WriteLine($"{new string(' ', indentLevel)}{file.Name} - {file.Length} bytes - {file.GetDOSAttributes()}");
            }

            foreach (var subDirectory in directory.GetDirectories())
            {
                Console.WriteLine($"{new string(' ', indentLevel)}{subDirectory.Name} - {subDirectory.GetFiles().Length} items - {subDirectory.GetDOSAttributes()}");
                DisplayDirectoryContents(subDirectory, indentLevel + 2);
            }
        }

        static Dictionary<string, long> LoadDirectoryContents(DirectoryInfo directory)
        {
            var collection = new Dictionary<string, long>();

            foreach (var file in directory.GetFiles())
            {
                collection.Add(file.Name, file.Length);
            }

            foreach (var subDirectory in directory.GetDirectories())
            {
                collection.Add(subDirectory.Name, subDirectory.GetFiles().Length);
            }

            return collection;
        }

        static void SerializeCollection(Dictionary<string, long> collection)
        {
            using (FileStream fs = new FileStream("collection.bin", FileMode.Create))
            {
                BinaryFormatter formatter = new BinaryFormatter();
                formatter.Serialize(fs, collection);
            }
        }

        static Dictionary<string, long> DeserializeCollection()
        {
            using (FileStream fs = new FileStream("collection.bin", FileMode.Open))
            {
                BinaryFormatter formatter = new BinaryFormatter();
                return (Dictionary<string, long>)formatter.Deserialize(fs);
            }
        }
    }

    static class Extensions
    {
        public static DateTime GetOldestItemDate(this DirectoryInfo directory)
        {
            DateTime oldestDate = DateTime.MaxValue;

            // Przetwarzaj pliki w bieżącym katalogu
            foreach (var file in directory.GetFiles())
            {
                if (file.LastWriteTime < oldestDate)
                {
                    oldestDate = file.LastWriteTime;
                }
            }

            // Przetwarzaj podkatalogi rekurencyjnie
            foreach (var subDirectory in directory.GetDirectories())
            {
                DateTime subDirOldestDate = subDirectory.GetOldestItemDate();
                if (subDirOldestDate < oldestDate)
                {
                    oldestDate = subDirOldestDate;
                }
            }

            return oldestDate;
        }
        public static string GetDOSAttributes(this FileSystemInfo info)
        {
            string attributes = "";

            if ((info.Attributes & FileAttributes.ReadOnly) == FileAttributes.ReadOnly)
                attributes += "r";
            if ((info.Attributes & FileAttributes.Archive) == FileAttributes.Archive)
                attributes += "a";
            if ((info.Attributes & FileAttributes.Hidden) == FileAttributes.Hidden)
                attributes += "h";
            if ((info.Attributes & FileAttributes.System) == FileAttributes.System)
                attributes += "s";

            return attributes;
        }
    }

    class StringLengthComparer : IComparer<string>
    {
        public int Compare(string x, string y)
        {
            if (x.Length != y.Length)
                return x.Length.CompareTo(y.Length);
            else
                return x.CompareTo(y);
        }
    }
}
