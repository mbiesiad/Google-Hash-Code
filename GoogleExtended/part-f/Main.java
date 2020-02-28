import com.Library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("f_libraries_of_the_world.txt");
        Scanner in = new Scanner(file);

        String firstLine = in.nextLine();
        String[] firstLineNumbers = firstLine.split(" ");
        Long books = Long.parseLong(firstLineNumbers[0]);
        Long libraries = Long.parseLong(firstLineNumbers[1]);
        Long scanningDays = Long.parseLong(firstLineNumbers[2]);

        String secondLine = in.nextLine();
        List<Long> booksScores = new ArrayList<>();
        for (String bookScore : secondLine.split(" ")) {
            booksScores.add(Long.parseLong(bookScore));
        }

        HashMap<Long, Long> booksIndexesAndScores = new HashMap<Long, Long>();
        Long k = 0L;
        for (String bookScore : secondLine.split(" ")) {
            booksIndexesAndScores.put(k, Long.parseLong(bookScore));
            k++;
        }

        List<Library> libraryObjs = new ArrayList<>();
        boolean readingFile = true;
        Long index = 0L;
        while (readingFile)
        {
            if (!in.hasNext()) {
                readingFile = false;
                break;
            }
            else {
                String line = in.nextLine();
                String[] libSpecifics = line.split(" ");
                Library library = new Library(index, Long.parseLong(libSpecifics[0]), Long.parseLong(libSpecifics[1]), Long.parseLong(libSpecifics[2]));

                String fourthLine = in.nextLine();
                String[] bookIndexes = fourthLine.split(" ");
                library.setBooksIndexes(booksIndexesAndScores, bookIndexes);

                libraryObjs.add(library);
                index++;
            }
        }

        Long totalShippedBooks1 = 0L;
        Long totalShippedBooks2 = 0L;

        Long tempScanningDays = new Long(scanningDays);

        for (Library library : libraryObjs) {
            library.calculateEffectiveShippedBooks(tempScanningDays);
            totalShippedBooks1 += library.getEffectiveShippedBooksPerScanningTime();
        }

        libraryObjs = libraryObjs.stream()
                .sorted(Comparator.comparingLong(Library::getSignUpDays))
                .collect(Collectors.toList());

        for (Library lib : libraryObjs) {
            lib.sortBooksScoresAndIndexes();
            lib.calculateMaximalGainedScoreOfEffectiveShippedBooksPerScanningTime();
        }

        PrintWriter writeSolution = new PrintWriter("f_byScan.txt");

        writeSolution.println(libraryObjs.size());

        for (int i = 0; i < libraryObjs.size(); i++) {
            writeSolution.print(libraryObjs.get(i).getIndex() + " " + libraryObjs.get(i).getEffectiveShippedBooksPerScanningTime());
            writeSolution.println();

            for (int j = 0; j < libraryObjs.get(i).getEffectiveShippedBooksPerScanningTime(); j++) {
                writeSolution.print(libraryObjs.get(i).getBookIndexes().get(j) + " ");
            }
            writeSolution.println();
        }

        writeSolution.close();
    }

    private static void recompositeBooksOrder(Library libraryObj) {
        Collections.sort(libraryObj.getBookScoresByIndexes());
        Collections.reverse(libraryObj.getBookScoresByIndexes());
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}