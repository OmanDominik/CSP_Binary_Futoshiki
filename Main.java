import binary.BinaryLayout;
import futoshiki.FutoshikiLayout;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        allTests();
    }

    public static void allTests(){
        long before;
        long after;

        File binary66File = new File("src/main/resources/binary_6x6");
        File binary88File = new File("src/main/resources/binary_8x8");
        File binary1010File = new File("src/main/resources/binary_10x10");
        File futoshiki44File = new File("src/main/resources/futoshiki_4x4");
        File futoshiki55File = new File("src/main/resources/futoshiki_5x5");
        File futoshiki66File = new File("src/main/resources/futoshiki_6x6");
        File futoshiki77File = new File("src/main/resources/futoshiki_7x7");

        BinaryLayout binary6 = new BinaryLayout(binary66File);
        BinaryLayout binary8 = new BinaryLayout(binary88File);
        BinaryLayout binary10 = new BinaryLayout(binary1010File);

        FutoshikiLayout futoshiki4 = new FutoshikiLayout(futoshiki44File);
        FutoshikiLayout futoshiki5 = new FutoshikiLayout(futoshiki55File);
        FutoshikiLayout futoshiki6 = new FutoshikiLayout(futoshiki66File);
        FutoshikiLayout futoshiki7 = new FutoshikiLayout(futoshiki77File);

        System.out.println("Backtrack:\n");

        before = System.currentTimeMillis();
        System.out.println("Binary 6x6 solutions:");
        binary6.backtrackSearch();
        System.out.println("Number of generated solutions for binary 6x6: " + binary6.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for binary 6x6: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nBinary 8x8 solutions:");
        binary8.backtrackSearch();
        System.out.println("Number of generated solutions for binary 8x8: " + binary8.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for binary 8x8: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nBinary 10x10 solutions:");
        binary10.backtrackSearch();
        System.out.println("Number of generated solutions for binary 10x10: " + binary10.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for binary 10x10: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 4x4 solutions:");
        futoshiki4.backtrackSearch();
        System.out.println("\nNumber of generated solutions for futoshiki 4x4: " + futoshiki4.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 4x4: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 5x5 solutions:");
        futoshiki5.backtrackSearch();
        System.out.println("\nNumber of generated solutions for futoshiki 5x5: " + futoshiki5.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 5x5: " + (double)(after-before)/1000d + "s");


        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 6x6 solutions:");
        futoshiki6.backtrackSearch();
        System.out.println("\nNumber of generated solutions for futoshiki 6x6: " + futoshiki6.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 6x6: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 7x7 solutions:");
        futoshiki7.backtrackSearch();
        System.out.println("\nNumber of generated solutions for futoshiki 7x7: " + futoshiki7.getBackTrackSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 7x7: " + (double)(after-before)/1000d + "s");

        System.out.println("\nForward:");

        before=System.currentTimeMillis();
        System.out.println("\nBinary 6x6 solutions:");
        binary6.forwardSearch();
        System.out.println("Number of generated solutions for binary 6x6: " + binary6.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for binary 6x6: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nBinary 8x8 solutions:");
        binary8.forwardSearch();
        System.out.println("Number of generated solutions for binary 8x8: " + binary8.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for binary 8x8: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nBinary 10x10 solutions:");
        binary10.forwardSearch();
        System.out.println("Number of generated solutions for binary 10x10: " + binary10.getForwardSolutions().size());
        after=System.currentTimeMillis();
        System.out.println("\nComputing time for binary 10x10: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 4x4 solutions:");
        futoshiki4.forwardSearch(true);
        System.out.println("\nNumber of generated solutions for futoshiki 4x4: " + futoshiki4.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 4x4 with heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 4x4 solutions:");
        futoshiki4.forwardSearch(false);
        System.out.println("\nNumber of generated solutions for futoshiki 4x4: " + futoshiki4.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 4x4 without heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 5x5 solutions:");
        futoshiki5.forwardSearch(true);
        System.out.println("\nNumber of generated solutions for futoshiki 5x5: " + futoshiki5.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 5x5 with heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 5x5 solutions:");
        futoshiki5.forwardSearch(false);
        System.out.println("\nNumber of generated solutions for futoshiki 5x5: " + futoshiki5.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 5x5 without heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 6x6 solutions:");
        futoshiki6.forwardSearch(true);
        System.out.println("\nNumber of generated solutions for futoshiki 6x6: " + futoshiki6.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 6x6 with heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 6x6 solutions:");
        futoshiki6.forwardSearch(false);
        System.out.println("\nNumber of generated solutions for futoshiki 6x6: " + futoshiki6.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 6x6 without heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 7x7 solutions:");
        futoshiki7.forwardSearch(true);
        System.out.println("\nNumber of generated solutions for futoshiki 7x7: " + futoshiki7.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 7x7 with heuristics: " + (double)(after-before)/1000d + "s");

        before = System.currentTimeMillis();
        System.out.println("\nFutoshiki 7x7 solutions:");
        futoshiki7.forwardSearch(false);
        System.out.println("\nNumber of generated solutions for futoshiki 7x7: " + futoshiki7.getForwardSolutions().size());
        after = System.currentTimeMillis();
        System.out.println("\nComputing time for futoshiki 7x7 without heuristics: " + (double)(after-before)/1000d + "s");
    }

}