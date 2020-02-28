import com.Library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("a_example.txt");
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

        String thirdLine = in.nextLine();
        String[] libSpecifics = thirdLine.split(" ");
        List<Library> libraryObjs = new ArrayList<>();
        Library library1 = new Library(Long.parseLong(libSpecifics[0]), Long.parseLong(libSpecifics[1]), Long.parseLong(libSpecifics[2]));

        String fourthLine = in.nextLine();
        String[] bookIndexes = fourthLine.split(" ");
        library1.setBooksIndexes(booksIndexesAndScores, bookIndexes);

        libraryObjs.add(library1);

        String fifthLine = in.nextLine();
        String[] libSpecifics2 = fifthLine.split(" ");
        Library library2 = new Library(Long.parseLong(libSpecifics2[0]), Long.parseLong(libSpecifics2[1]), Long.parseLong(libSpecifics2[2]));

        String sixthLine = in.nextLine();
        String[] bookIndexes2 = sixthLine.split(" ");
        library2.setBooksIndexes(booksIndexesAndScores, bookIndexes2);

        libraryObjs.add(library2);

        Long totalShippedBooks1 = 0L;
        Long totalShippedBooks2 = 0L;

        Long tempScanningDays = new Long(scanningDays);
        for (Library library : libraryObjs) {
            library.calculateEffectiveShippedBooks(tempScanningDays);
            totalShippedBooks1 += library.getEffectiveShippedBooksPerScanningTime();
            tempScanningDays -= library.getSignUpDays();
        }

        tempScanningDays = new Long(scanningDays);
        for (int i = libraryObjs.size(); i-- > 0; ) {
            libraryObjs.get(i).calculateEffectiveShippedBooks(tempScanningDays);
            totalShippedBooks2 +=  libraryObjs.get(i).getEffectiveShippedBooksPerScanningTime();
            tempScanningDays -=  libraryObjs.get(i).getSignUpDays();
        }

        PrintWriter writeSolution = new PrintWriter("a.txt");

        writeSolution.println(libraryObjs.size());
        for (int i = libraryObjs.size(); i-- > 0; ) {
            writeSolution.print(i + " " + libraryObjs.get(i).getEffectiveShippedBooksPerScanningTime());
            writeSolution.println();

            List<Long> tempBookScoreList = new ArrayList<>();
            booksIndexesAndScores.values().stream().sorted();
            if (libraryObjs.get(i).getBookIndexes().size() > libraryObjs.get(i).getEffectiveShippedBooksPerScanningTime()) {
                recompositeBooksOrder(libraryObjs.get(i));
            }
            for(int j = 0; j < libraryObjs.get(i).getEffectiveShippedBooksPerScanningTime(); j++) {
                writeSolution.print(getKey(booksIndexesAndScores, libraryObjs.get(i).getBookScoresByIndexes().get(j)) + " ");
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
