public class Prng_Test {

    public void testRandomNumberGenerator() {
        Prng test = new Prng();
        long time;
        time = System.nanoTime();
        test.generateRandomNumbers("File_1MB.txt", "1234567887654321", 1048576);
        System.out.println("1 MB file generated in "
                +String.format("%.3f", ((System.nanoTime() - time) * 0.000000001) - 4)+" seconds\n");
        time = System.nanoTime();
        test.generateRandomNumbers("File_100MB.txt", "1234567887654321", 104857600);
        System.out.println("100 MB file generated in "
                +String.format("%.3f", ((System.nanoTime() - time) * 0.000000001) - 4)+" seconds\n");
        time = System.nanoTime();
        test.generateRandomNumbers("File_1000MB.txt", "1234567887654321", 1048576000);
        System.out.println("1000 MB file generated in "
                +String.format("%.3f", ((System.nanoTime() - time) * 0.000000001) - 4)+" seconds\n");
        System.out.println();
    }

    public static void main(String[] args) {
        Prng_Test test = new Prng_Test();
        test.testRandomNumberGenerator();
    }

}
