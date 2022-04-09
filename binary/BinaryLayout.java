package binary;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BinaryLayout {
    int fieldCounter = 0;
    private File sourceFile;
    private List<String> layout;
    private List<List<Integer>> integerLayout;
    private int sideLength;
    private HashMap<Integer, List<Integer>> startingValuesIndexes;
    private List<Integer> domain;
    List<List<List<Integer>>> backTrackSolutions;
    List<List<List<Integer>>> forwardSolutions;
    List<Integer>[][] domainBoard;

    public BinaryLayout(File sourceFile) {
        this.sourceFile = sourceFile;
        domain = new ArrayList<>(List.of(0, 1));

        try {
            layout = FileUtils.readLines(sourceFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sideLength = layout.get(0).length();
        this.startingValuesIndexes = new HashMap<>();

        for (int i = 0; i < sideLength; i++) {
            this.startingValuesIndexes.put(i, new ArrayList<>());
            for (int j = 0; j < sideLength; j++) {
                if (layout.get(i).charAt(j) != 'x') {
                    this.startingValuesIndexes.get(i).add(j);
                }
            }
        }

        this.integerLayout = new ArrayList<>();
        for (int i = 0; i < sideLength; i++) {
            this.integerLayout.add(new ArrayList<>());
            for (int j = 0; j < sideLength; j++) {
                if (layout.get(i).charAt(j) != 'x') {
                    this.integerLayout.get(i).add(Character.getNumericValue(layout.get(i).charAt(j)));
                } else {
                    this.integerLayout.get(i).add(-1);
                }
            }
        }

        this.backTrackSolutions = new ArrayList<>();
        this.forwardSolutions = new ArrayList<>();

        this.domainBoard = new List[this.sideLength][this.sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                this.domainBoard[i][j] = new ArrayList<>();
                if (integerLayout.get(i).get(j) != -1) {
                    this.domainBoard[i][j].add(integerLayout.get(i).get(j));
                } else {
                    this.domainBoard[i][j].addAll(domain);
                }
            }
        }
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public List<String> getLayout() {
        return layout;
    }

    public void setLayout(List<String> layout) {
        this.layout = layout;
    }

    public int getSideLength() {
        return sideLength;
    }

    public void setSideLength(int sideLength) {
        this.sideLength = sideLength;
    }

    public HashMap<Integer, List<Integer>> getStartingValuesIndexes() {
        return startingValuesIndexes;
    }

    public void setStartingValuesIndexes(HashMap<Integer, List<Integer>> startingValuesIndexes) {
        this.startingValuesIndexes = startingValuesIndexes;
    }

    public List<Integer> getDomain() {
        return domain;
    }

    public void setDomain(List<Integer> domain) {
        this.domain = domain;
    }

    public List<List<Integer>> getIntegerLayout() {
        return integerLayout;
    }

    public List<List<List<Integer>>> getBackTrackSolutions() {
        return backTrackSolutions;
    }

    public void setBackTrackSolutions(List<List<List<Integer>>> backTrackSolutions) {
        this.backTrackSolutions = backTrackSolutions;
    }

    public void setIntegerLayout(List<List<Integer>> integerLayout) {
        this.integerLayout = integerLayout;
    }

    public List<List<List<Integer>>> getForwardSolutions() {
        return forwardSolutions;
    }

    public void setForwardSolutions(List<List<List<Integer>>> forwardSolutions) {
        this.forwardSolutions = forwardSolutions;
    }

    public List<Integer>[][] getDomainBoard() {
        return domainBoard;
    }

    public void setDomainBoard(List<Integer>[][] domainBoard) {
        this.domainBoard = domainBoard;
    }

    public void backtrackSearch() {
        fieldCounter = 0;
        this.backTrackSolutions.clear();
        backtrack(integerLayout, 0, 0);
        System.out.println("Number of visited fields: " + fieldCounter);
        //printSolutions(this.backTrackSolutions);
    }

    public void forwardSearch() {
        fieldCounter = 0;
        this.forwardSolutions.clear();
        int[][] visited = initVisited();
        List<Integer>[][] domainBoard = deepCopy(this.domainBoard);
        forward(domainBoard, visited);
        System.out.println("Number of visited fields: " + fieldCounter);
        //printSolutions(this.forwardSolutions);
    }

    public void printSolutions(List<List<List<Integer>>> solutions) {
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("______");
            for (int j = 0; j < solutions.get(0).size(); j++) {
                for (int k = 0; k < solutions.get(0).size(); k++) {
                    System.out.print(solutions.get(i).get(j).get(k) + " ");
                }
                System.out.print("\n");
            }
        }
    }

    private void backtrack(List<List<Integer>> layout, int row, int column) {
        List<List<Integer>> tempLayout = new ArrayList<>();

        for (List<Integer> sublist : layout) {
            tempLayout.add(new ArrayList<>(sublist));
        }

        if (row == sideLength) {
            backTrackSolutions.add(tempLayout);
        } else {
            fieldCounter++;
            if (layout.get(row).get(column) != -1) {
                if (rowsValidator(tempLayout) && columnsValidator(tempLayout)) {
                    if (column == sideLength - 1)
                        backtrack(tempLayout, row + 1, 0);
                    else
                        backtrack(tempLayout, row, column + 1);
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    tempLayout.get(row).set(column, i);

                    if (rowsValidator(tempLayout) && columnsValidator(tempLayout)) {
                        if (column == sideLength - 1)
                            backtrack(tempLayout, row + 1, 0);
                        else
                            backtrack(tempLayout, row, column + 1);
                    }
                }
            }
        }

    }

    private boolean rowsValidator(List<List<Integer>> layout) {
        int counter;
        int previous;
        int counterOfZeros;
        int counterOfOnes;
        int isRowFull;
        int rowCounter = 0;
        HashSet<List<Integer>> uniqueRows = new HashSet<>();
        List<Integer> temporaryRow = new ArrayList<>();

        for (List<Integer> row : layout) {
            counter = 0;
            previous = -2;
            counterOfOnes = 0;
            counterOfZeros = 0;
            isRowFull = 1;
            temporaryRow.clear();
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == previous) {
                    counter++;
                } else {
                    counter = 0;
                }
                if ((counter == 2) && (row.get(i) != -1)) {
                    return false;
                }
                previous = row.get(i);

                if (row.get(i) == 1) {
                    counterOfOnes++;
                }
                if (row.get(i) == 0) {
                    counterOfZeros++;
                }

                temporaryRow.add(row.get(i));
                if (row.get(i) == -1) {
                    temporaryRow.clear();
                    isRowFull = 0;
                }

            }
            if ((counterOfOnes != counterOfZeros) && isRowFull == 1)
                return false;
            rowCounter += isRowFull;
            if (temporaryRow.size() == row.size()) {
                uniqueRows.add(new ArrayList<>(temporaryRow));
            }
        }
        if (uniqueRows.size() != rowCounter)
            return false;

        return true;
    }

    private boolean columnsValidator(List<List<Integer>> layout) {
        int counter;
        int previous;
        int counterOfZeros;
        int counterOfOnes;
        int isColumnFull;
        int columnCounter = 0;
        HashSet<List<Integer>> uniqueColumns = new HashSet<>();
        List<Integer> temporaryColumn = new ArrayList<>();

        for (int j = 0; j < layout.get(0).size(); j++) {
            counter = 0;
            previous = -2;
            counterOfOnes = 0;
            counterOfZeros = 0;
            isColumnFull = 1;
            temporaryColumn.clear();
            for (int i = 0; i < layout.get(0).size(); i++) {
                if (layout.get(i).get(j) == previous) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == 2 && layout.get(i).get(j) != -1) {
                    return false;
                }
                previous = layout.get(i).get(j);

                if (layout.get(i).get(j) == 1) {
                    counterOfOnes++;
                }
                if (layout.get(i).get(j) == 0) {
                    counterOfZeros++;
                }

                temporaryColumn.add(layout.get(i).get(j));
                if (layout.get(i).get(j) == -1) {
                    temporaryColumn.clear();
                    isColumnFull = 0;
                }
            }
            if ((counterOfOnes != counterOfZeros) && (isColumnFull == 1))
                return false;
            columnCounter += isColumnFull;
            if (temporaryColumn.size() == layout.get(0).size()) {
                uniqueColumns.add(temporaryColumn);
            }
        }
        if (uniqueColumns.size() != columnCounter)
            return false;

        return true;
    }

    private void forward(List<Integer>[][] domainBoard, int[][] visited) {

        if (error(domainBoard))
            return;

        if (correct(domainBoard, visited)) {
            addSolution(domainBoard);
            return;
        }
        fieldCounter++;
        int row = 0;
        int col = 0;

        int minDomain = sideLength + 1;
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                if (visited[i][j] == 0 && domainBoard[i][j].size() < minDomain) {
                    row = i;
                    col = j;
                    minDomain = domainBoard[i][j].size();
                    if (minDomain == 1)
                        break;
                }
            }
        }

        if (minDomain != sideLength + 1) {
            visited[row][col] = 1;
            for (int value : domainBoard[row][col]) {
                List<Integer>[][] updatedDomain = deepCopy(domainBoard);
                updatedDomain[row][col] = new ArrayList<>(List.of(value));
                updatedDomain = forwardCheck(updatedDomain, row, col);
                forward(updatedDomain, visited);
            }
            visited[row][col] = 0;
        } else
            return;
    }

    private void addSolution(List<Integer>[][] domainBoard) {
        List<List<Integer>> solution = new ArrayList<>();

        for (int i = 0; i < sideLength; i++) {
            solution.add(new ArrayList<>());
            for (int j = 0; j < sideLength; j++) {
                solution.get(i).add(domainBoard[i][j].get(0));
            }
        }

        this.forwardSolutions.add(solution);
    }

    private List<Integer>[][] forwardCheck(List<Integer>[][] domainBoard, int row, int column) {
        if (domainBoard[row][column].size() != 1)
            return domainBoard;
        int val = domainBoard[row][column].get(0);

        int rowCounter = 0;
        int colCounter = 0;
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < i; j++) {
                if (domainBoard[row][j].size() != 0 && domainBoard[j][column].size() != 0) {
                    if (domainBoard[row][j].get(0) == val && domainBoard[row][j].size() == 1)
                        rowCounter++;
                    if (domainBoard[j][column].get(0) == val && domainBoard[j][column].size() == 1)
                        colCounter++;
                }
            }
            if (i != column && domainBoard[row][i].contains(val) && rowCounter >= sideLength / 2) {
                domainBoard[row][i].remove(domainBoard[row][i].indexOf(val));
            }
            if (i != row && domainBoard[i][column].contains(val) && colCounter >= sideLength / 2) {
                domainBoard[i][column].remove(domainBoard[i][column].indexOf(val));
            }

            if (i != column && i > 2) {
                if (domainBoard[row][i].contains(val) &&
                        domainBoard[row][i - 1].size() == 1 && domainBoard[row][i - 1].get(0) == val &&
                        domainBoard[row][i - 2].size() == 1 && domainBoard[row][i - 2].get(0) == val) {
                    domainBoard[row][i].remove(domainBoard[row][i].indexOf(val));
                }
            }

            if (i != row && i > 2) {
                if (domainBoard[i][column].contains(val) &&
                        domainBoard[i - 1][column].size() == 1 && domainBoard[i - 1][column].get(0) == val &&
                        domainBoard[i - 2][column].size() == 1 && domainBoard[i - 2][column].get(0) == val) {
                    domainBoard[i][column].remove(domainBoard[i][column].indexOf(val));
                }
            }
            rowCounter = 0;
            colCounter = 0;

        }

        return domainBoard;
    }

    private List<Integer>[][] deepCopy(List<Integer>[][] domainBoard) {
        List<Integer>[][] result;

        result = new List[this.sideLength][this.sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                result[i][j] = new ArrayList<>(domainBoard[i][j]);
            }
        }
        return result;
    }

    private boolean error(List<Integer>[][] domainBoard) {
        for (List<Integer>[] domainRow : domainBoard) {
            for (List<Integer> domainCell : domainRow) {
                if (domainCell.size() == 0)
                    return true;
            }
        }
        List<List<Integer>> rows = new ArrayList<>();
        List<List<Integer>> cols = new ArrayList<>();

        for (int i = 0; i < sideLength; i++) {
            List<Integer> row = new ArrayList<>();
            List<Integer> col = new ArrayList<>();
            for (int j = 0; j < sideLength; j++) {
                if (domainBoard[i][j].size() == 1) {
                    row.add(domainBoard[i][j].get(0));
                }
                if (domainBoard[j][i].size() == 1) {
                    col.add(domainBoard[j][i].get(0));
                }
            }
            if (row.size() == sideLength) {
                if (rows.contains(row))
                    return true;
                rows.add(row);
            }
            if (col.size() == sideLength) {
                if (cols.contains(col))
                    return true;
                cols.add(col);
            }
        }

        return false;
    }

    private boolean correct(List<Integer>[][] domainBoard, int[][] visited) {
        for (List<Integer>[] domainRow : domainBoard) {
            for (List<Integer> domainCell : domainRow) {
                if (domainCell.size() != 1)
                    return false;
            }
        }

        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                if (visited[i][j] == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private int[][] initVisited() {
        int[][] visited = new int[sideLength][sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                if (domainBoard[i][j].size() == 1)
                    visited[i][j] = 1;
                else
                    visited[i][j] = 0;
            }
        }
        return visited;
    }

}
