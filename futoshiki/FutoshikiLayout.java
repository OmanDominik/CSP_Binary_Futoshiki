package futoshiki;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FutoshikiLayout {
    int fieldCounter = 0;
    private File sourceFile;
    private List<String> layout;
    private int sideLength;
    private HashMap<Integer, List<Integer>> startingValuesIndexes;
    private List<String> verticalConstraints;
    private List<String> horizontalConstraints;
    private List<String> numbersLayout;
    private List<List<Integer>> integerLayout;
    private List<Integer> domain;
    List<List<List<Integer>>> backTrackSolutions;
    List<List<List<Integer>>> forwardSolutions;
    List<Integer>[][] domainBoard;

    public FutoshikiLayout(File sourceFile) {
        this.sourceFile = sourceFile;

        try {
            layout = FileUtils.readLines(sourceFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sideLength = (layout.get(0).length() + 1) / 2;

        this.numbersLayout = new ArrayList<>();
        this.verticalConstraints = new ArrayList<>();
        this.horizontalConstraints = new ArrayList<>();

        for (int i = 0; i < sideLength; i++) {
            this.numbersLayout.add(new String());
            this.verticalConstraints.add(new String());
            if (i < this.sideLength - 1) {
                this.horizontalConstraints.add(new String());
            }
        }

        for (int i = 0; i < layout.get(0).length(); i++) {
            for (int j = 0; j < layout.get(0).length(); j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    this.numbersLayout.set(i / 2, numbersLayout.get(i / 2).concat(String.valueOf(layout.get(i).charAt(j))));
                }
                if (i % 2 == 0 && j % 2 == 1) {
                    this.verticalConstraints.set(i / 2, verticalConstraints.get(i / 2).concat(String.valueOf(layout.get(i).charAt(j))));
                }
                if (j < this.sideLength) {
                    if (i % 2 == 1) {
                        this.horizontalConstraints.set(i / 2, horizontalConstraints.get(i / 2).concat(String.valueOf(layout.get(i).charAt(j))));
                    }
                }
            }
        }

        this.startingValuesIndexes = new HashMap<>();

        for (int i = 0; i < sideLength; i++) {
            this.startingValuesIndexes.put(i, new ArrayList<>());
            for (int j = 0; j < sideLength; j++) {
                if (numbersLayout.get(i).charAt(j) != 'x') {
                    this.startingValuesIndexes.get(i).add(j);
                }
            }
        }

        domain = new ArrayList<>();
        for (int i = 1; i <= numbersLayout.size(); i++) {
            domain.add(i);
        }


        this.integerLayout = new ArrayList<>();
        for (int i = 0; i < sideLength; i++) {
            this.integerLayout.add(new ArrayList<>());
            for (int j = 0; j < sideLength; j++) {
                if (numbersLayout.get(i).charAt(j) != 'x') {
                    this.integerLayout.get(i).add(Character.getNumericValue(numbersLayout.get(i).charAt(j)));
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

    public List<String> getVerticalConstraints() {
        return verticalConstraints;
    }

    public void setVerticalConstraints(List<String> verticalConstraints) {
        this.verticalConstraints = verticalConstraints;
    }

    public List<String> getHorizontalConstraints() {
        return horizontalConstraints;
    }

    public void setHorizontalConstraints(List<String> horizontalConstraints) {
        this.horizontalConstraints = horizontalConstraints;
    }

    public List<String> getNumbersLayout() {
        return numbersLayout;
    }

    public void setNumbersLayout(List<String> numbersLayout) {
        this.numbersLayout = numbersLayout;
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

    public void setIntegerLayout(List<List<Integer>> integerLayout) {
        this.integerLayout = integerLayout;
    }

    public List<List<List<Integer>>> getBackTrackSolutions() {
        return backTrackSolutions;
    }

    public void setBackTrackSolutions(List<List<List<Integer>>> backTrackSolutions) {
        this.backTrackSolutions = backTrackSolutions;
    }

    public List<List<List<Integer>>> getForwardSolutions() {
        return forwardSolutions;
    }

    public void setForwardSolutions(List<List<List<Integer>>> forwardSolutions) {
        this.forwardSolutions = forwardSolutions;
    }

    public void backtrackSearch() {
        fieldCounter = 0;
        this.backTrackSolutions.clear();
        backtrack(integerLayout, 0, 0);
        System.out.println("Number of visited fields: " + fieldCounter);
        //printSolutions(this.backTrackSolutions);
    }

    public void forwardSearch(boolean heuristicFlag) {
        fieldCounter = 0;
        this.forwardSolutions.clear();
        int[][] visited = initVisited();
        List<Integer>[][] domainBoard = deepCopy(this.domainBoard);
        update(domainBoard);
        forward(domainBoard, visited, heuristicFlag);
        System.out.println("Number of visited fields: " + fieldCounter);
        //printSolutions(this.forwardSolutions);
    }

    public void printSolutions(List<List<List<Integer>>> solutions) {
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("\n________________________");
            for (int j = 0; j < solutions.get(0).size(); j++) {
                for (int k = 0; k < solutions.get(0).size(); k++) {
                    System.out.print(solutions.get(i).get(j).get(k) + " ");
                    if (k < sideLength - 1)
                        System.out.print(this.verticalConstraints.get(j).charAt(k) + " ");
                }
                if (j < sideLength - 1) {
                    System.out.println();
                    for (int k = 0; k < sideLength; k++)
                        System.out.print(this.horizontalConstraints.get(j).charAt(k) + "   ");
                    System.out.println();
                }
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
                if (rowsValidator(tempLayout, row) && columnsValidator(tempLayout, column)) {
                    if (column == sideLength - 1) {
                        backtrack(tempLayout, row + 1, 0);
                    } else
                        backtrack(tempLayout, row, column + 1);
                }
            } else {
                for (int i = 0; i < sideLength; i++) {
                    tempLayout.get(row).set(column, (i + 1));

                    if (rowsValidator(tempLayout, row) && columnsValidator(tempLayout, column)) {
                        if (column == sideLength - 1)
                            backtrack(tempLayout, row + 1, 0);
                        else
                            backtrack(tempLayout, row, column + 1);
                    }
                }
            }
        }

    }

    private boolean rowsValidator(List<List<Integer>> layout, int row) {
        List<Integer> temporaryRow = new ArrayList<>();

        for (int j = 0; j < sideLength; j++) {
            if (layout.get(row).get(j) != -1)
                if (temporaryRow.contains(layout.get(row).get(j)))
                    return false;

            temporaryRow.add(layout.get(row).get(j));

            if (j < layout.get(row).size() - 1) {
                if (this.verticalConstraints.get(row).charAt(j) == '>' && layout.get(row).get(j + 1) != -1 && layout.get(row).get(j) != -1) {
                    if (layout.get(row).get(j) <= layout.get(row).get(j + 1))
                        return false;
                } else if (this.verticalConstraints.get(row).charAt(j) == '<' && layout.get(row).get(j + 1) != -1 && layout.get(row).get(j) != -1) {
                    if (layout.get(row).get(j) >= layout.get(row).get(j + 1))
                        return false;
                }
            }
        }


        return true;
    }

    private boolean columnsValidator(List<List<Integer>> layout, int column) {
        List<Integer> temporaryColumn = new ArrayList<>();

        for (int j = 0; j < layout.get(column).size(); j++) {
            if (layout.get(j).get(column) != -1)
                if (temporaryColumn.contains(layout.get(j).get(column)))
                    return false;

            temporaryColumn.add(layout.get(j).get(column));

            if (j < layout.get(column).size() - 1) {
                if (this.horizontalConstraints.get(j).charAt(column) == '>' && layout.get(j + 1).get(column) != -1 && layout.get(j).get(column) != -1) {
                    if (layout.get(j).get(column) <= layout.get(j + 1).get(column))
                        return false;
                } else if (this.horizontalConstraints.get(j).charAt(column) == '<' && layout.get(j + 1).get(column) != -1 && layout.get(j).get(column) != -1) {
                    if (layout.get(j).get(column) >= layout.get(j + 1).get(column))
                        return false;
                }
            }
        }

        return true;
    }

    private void forward(List<Integer>[][] domainBoard, int[][] visited, boolean heuristicFlag) {

        if (error(domainBoard))
            return;

        if (correct(domainBoard, visited)) {
            addSolution(domainBoard);
            return;
        }
        fieldCounter++;
        int row = 0;
        int col = 0;

        if (heuristicFlag) {
            int minDomain = sideLength + 1;
            for (int i = 0; i < sideLength; i++) {
                for (int j = 0; j < sideLength; j++) {
                    if (visited[i][j] == 0 && domainBoard[i][j].size() < minDomain) {
                        row = i;
                        col = j;
                        minDomain = domainBoard[i][j].size();
                    } else if (visited[i][j] == 0 && domainBoard[i][j].size() == minDomain) {
                        if (constraintsIntensity(i, j) > constraintsIntensity(row, col)) {
                            row = i;
                            col = j;
                        }
                    }
                }
            }

            if (minDomain != sideLength + 1) {
                visited[row][col] = 1;
                for (int value : domainBoard[row][col]) {
                    List<Integer>[][] updatedDomain = deepCopy(domainBoard);
                    updatedDomain[row][col] = new ArrayList<>(List.of(value));
                    updatedDomain = forwardCheck(updatedDomain, row, col);
                    forward(updatedDomain, visited, heuristicFlag);
                }
                visited[row][col] = 0;
            } else
                return;
        } else {
            boolean isPossible = false;
            for (int i = 0; i < sideLength; i++) {
                for (int j = 0; j < sideLength; j++) {
                    if (visited[i][j] == 0) {
                        row = i;
                        col = j;
                        isPossible = true;
                        break;
                    }
                }
            }

            if (isPossible) {
                visited[row][col] = 1;
                for (int value : domainBoard[row][col]) {
                    List<Integer>[][] updatedDomain = deepCopy(domainBoard);
                    updatedDomain[row][col] = new ArrayList<>(List.of(value));
                    updatedDomain = forwardCheck(updatedDomain, row, col);
                    forward(updatedDomain, visited, heuristicFlag);
                }
                visited[row][col] = 0;
            } else
                return;
        }
    }

    private int constraintsIntensity(int row, int col) {
        int counter = 0;
        if (row < sideLength - 1)
            if (this.horizontalConstraints.get(row).charAt(col) != '-')
                counter++;

        if (col < sideLength - 1)
            if (this.verticalConstraints.get(row).charAt(col) != '-')
                counter++;

        return counter;
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

        for (int i = 0; i < sideLength; i++) {
            if (i != column && domainBoard[row][i].contains(val)) {
                domainBoard[row][i].remove(domainBoard[row][i].indexOf(val));
            }
            if (i != row && domainBoard[i][column].contains(val)) {
                domainBoard[i][column].remove(domainBoard[i][column].indexOf(val));
            }
        }

        domainBoard = update(domainBoard);
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

    private List<Integer>[][] update(List<Integer>[][] domainBoard) {
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                if (j < sideLength - 1) {
                    if (this.verticalConstraints.get(i).charAt(j) == '>' && domainBoard[i][j].size() == 1) {
                        for (int k = 0; k < domainBoard[i][j + 1].size(); k++) {
                            if (domainBoard[i][j + 1].get(k) > domainBoard[i][j].get(0)) {
                                domainBoard[i][j + 1].remove(k);
                                k--;
                            }
                        }
                    }
                    if (this.verticalConstraints.get(i).charAt(j) == '<' && domainBoard[i][j].size() == 1) {
                        for (int k = 0; k < domainBoard[i][j + 1].size(); k++) {
                            if (domainBoard[i][j + 1].get(k) < domainBoard[i][j].get(0)) {
                                domainBoard[i][j + 1].remove(k);
                                k--;
                            }
                        }
                    }
                }
                if (i < sideLength - 1) {
                    if (this.horizontalConstraints.get(i).charAt(j) == '>' && domainBoard[i][j].size() == 1) {
                        for (int k = 0; k < domainBoard[i + 1][j].size(); k++) {
                            if (domainBoard[i + 1][j].get(k) > domainBoard[i][j].get(0)) {
                                domainBoard[i + 1][j].remove(k);
                                k--;
                            }
                        }
                    }
                    if (this.horizontalConstraints.get(i).charAt(j) == '<' && domainBoard[i][j].size() == 1) {
                        for (int k = 0; k < domainBoard[i + 1][j].size(); k++) {
                            if (domainBoard[i + 1][j].get(k) < domainBoard[i][j].get(0)) {
                                domainBoard[i + 1][j].remove(k);
                                k--;
                            }
                        }
                    }
                }
            }
        }
        return domainBoard;
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
