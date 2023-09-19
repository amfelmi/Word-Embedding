import org.apache.commons.lang3.time.StopWatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SemanticMain {
    public List<String> listVocabulary = new ArrayList<>();  //List that contains all the vocabularies loaded from the csv file.
    public List<double[]> listVectors = new ArrayList<>(); //Associated vectors from the csv file.
    public List<Glove> listGlove = new ArrayList<>();
    public final List<String> STOPWORDS;

    public SemanticMain() throws IOException {
        STOPWORDS = Toolkit.loadStopWords();
        Toolkit.loadGLOVE();
    }


    public static void main(String[] args) throws IOException {
        StopWatch mySW = new StopWatch();
        mySW.start();
        SemanticMain mySM = new SemanticMain();
        mySM.listVocabulary = Toolkit.getListVocabulary();
        mySM.listVectors = Toolkit.getlistVectors();
        mySM.listGlove = mySM.CreateGloveList();

        List<CosSimilarityPair> listWN = mySM.WordsNearest("computer");
        Toolkit.PrintSemantic(listWN, 5);

        listWN = mySM.WordsNearest("phd");
        Toolkit.PrintSemantic(listWN, 5);

        List<CosSimilarityPair> listLA = mySM.LogicalAnalogies("china", "uk", "london", 5);
        Toolkit.PrintSemantic("china", "uk", "london", listLA);

        listLA = mySM.LogicalAnalogies("woman", "man", "king", 5);
        Toolkit.PrintSemantic("woman", "man", "king", listLA);

        listLA = mySM.LogicalAnalogies("banana", "apple", "red", 3);
        Toolkit.PrintSemantic("banana", "apple", "red", listLA);
        mySW.stop();

        if (mySW.getTime() > 2000)
            System.out.println("It takes too long to execute your code!\nIt should take less than 2 second to run.");
        else
            System.out.println("Well done!\nElapsed time in milliseconds: " + mySW.getTime());
    }

    public List<Glove> CreateGloveList() {
        List<Glove> listResult = new ArrayList<>();
        //TODO Task 6.1
        for (int i = 0; i < listVocabulary.size(); i ++) {
            String word = listVocabulary.get(i);
            double[] vectors = listVectors.get(i);
            if (!STOPWORDS.contains(word)) {
                Glove g = new Glove(word, new Vector(vectors));
                listResult.add(g);
            }
        }
        return listResult;
    }

    public List<CosSimilarityPair> WordsNearest(String _word) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.2
        Vector v = null;

        if(!listVocabulary.contains(_word) || STOPWORDS.contains(_word)) {
            _word = "error";
        }

        for (int i = 0; i < listGlove.size(); i++) {
            if (listGlove.get(i).getVocabulary().equals(_word)) {
                v = listGlove.get(i).getVector();
                break;
            }
        }

        for (int i = 0; i < listGlove.size(); i++) {
            if (!_word.equals(listGlove.get(i).getVocabulary())) {
                listCosineSimilarity.add(new CosSimilarityPair(_word, listGlove.get(i).getVocabulary(), listGlove.get(i).getVector().cosineSimilarity(v)));
            }
        }
        return HeapSort.doHeapSort(listCosineSimilarity);
    }

    public List<CosSimilarityPair> WordsNearest(Vector _vector) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.3
        for (int i = 0; i < listGlove.size(); i++) {
            CosSimilarityPair temp = new CosSimilarityPair(_vector, listGlove.get(i).getVocabulary(), listGlove.get(i).getVector().cosineSimilarity(_vector));
            if (temp.getCosineSimilarity() < 1)
                    listCosineSimilarity.add(temp);
                }
        return HeapSort.doHeapSort(listCosineSimilarity);
    }

    /**
     * Method to calculate the logical analogies by using references.
     * <p>
     * Example: uk is to london as china is to XXXX.
     *       _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference. Moreover, "XXXX" is the vocabulary(ies) we'd like
     * to get from this method.
     * <p>
     * If _top <= 0, then returns an empty listResult.
     * If the vocabulary list does not include _secISRef or _firISRef or _firTORef, then returns an empty listResult.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _top      How many vocabularies to include.
     */
    public List<CosSimilarityPair> LogicalAnalogies(String _secISRef, String _firISRef, String _firTORef, int _top) {
        List<CosSimilarityPair> listResult = new ArrayList<>();
        //TODO Task 6.4
        Vector firstIsVec = null;
        Vector firstToVec = null;
        Vector secondVec = null;

        for (int i = 0; i < listGlove.size(); i++) {
            if (listGlove.get(i).getVocabulary().equals(_firISRef)) {
                firstIsVec = listGlove.get(i).getVector();
            } else if (listGlove.get(i).getVocabulary().equals(_firTORef)) {
                firstToVec = listGlove.get(i).getVector();
            } else if (listGlove.get(i).getVocabulary().equals(_secISRef)) {
                secondVec = listGlove.get(i).getVector();
            } if (firstIsVec != null && firstToVec != null && secondVec != null) break;
        }
        if (_top <= 0 || firstIsVec == null || firstToVec == null || secondVec == null) {
            return listResult;
        }
        listResult = WordsNearest(secondVec.subtraction(firstIsVec).add(firstToVec));

        HeapSort.doHeapSort(listResult);
        List<CosSimilarityPair> finalList = new ArrayList<>();
        int ceiling = 0;

        for (int i = 0; i < listResult.size(); i++) {
            if (!listResult.get(i).getWord2().equals(_firTORef) && !listResult.get(i).getWord2().equals(_secISRef)) {
                finalList.add(listResult.get(i));
                ceiling++;
                if (_top == ceiling) break;
            }
        }
        return finalList;
    }
}