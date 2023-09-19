
public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 1.1
        this.doubElements = _elements;      //Double check this!
    }

    public double getElementatIndex(int _index) { //does index mean position in elements array?
        //TODO Task 1.2
        if (_index >= doubElements.length) {
            return -1;
        } else return doubElements[_index];
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 1.3
        if (_index >= doubElements.length) {
            doubElements[doubElements.length - 1] = _value;    // Does this modify the last element of array?
        } else doubElements[_index] = _value;       //does it want to override the element at index with value?
    }

    public double[] getAllElements() {
        //TODO Task 1.4
        return doubElements;
    }

    public int getVectorSize() {
        //TODO Task 1.5
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        //TODO Task 1.6

        Vector v = new Vector(doubElements);
        if (_size == doubElements.length || _size <= 0) {
            return v;
        }
        double[] resizedArray = new double[_size];
        int ceiling = Math.min(_size, doubElements.length);
        System.arraycopy(doubElements, 0, resizedArray, 0, ceiling);
        for (int i = 0; i < resizedArray.length; i++) {
            if (i >= ceiling) {
                resizedArray[i] = -1;
            }
        }
        v = new Vector(resizedArray);
        return v;
    }

    public Vector add(Vector _v) {
        //TODO Task 1.7
        Vector v = new Vector(doubElements);
        int roof = Math.max(v.getVectorSize(), _v.getVectorSize());
        double[] newArray = new double[roof];

        if (_v.getVectorSize() < v.getVectorSize()) {   //attempt to make 0 if statements here
            _v = _v.reSize(v.getVectorSize());
        }
        if (_v.getVectorSize() > v.getVectorSize()) {
            v = v.reSize(_v.getVectorSize());
        }
        for (int i = 0; i < v.getVectorSize(); i++) {
            newArray[i] = v.getElementatIndex(i) + _v.getElementatIndex(i);
        }

        v = new Vector(newArray);
        return v;
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 1.8
        Vector v = new Vector(doubElements);
        int roof = Math.max(v.getVectorSize(), _v.getVectorSize());
        double[] newArray = new double[roof];

        if (_v.getVectorSize() < v.getVectorSize()) {   //attempt to make 0 if statements here
            _v = _v.reSize(v.getVectorSize());
        }
        if (_v.getVectorSize() > v.getVectorSize()) {
            v = v.reSize(_v.getVectorSize());
        }
        for (int i = 0; i < v.getVectorSize(); i++) {
            newArray[i] = v.getElementatIndex(i) - _v.getElementatIndex(i);
        }

        v = new Vector(newArray);
        return v;
    }

    public double dotProduct(Vector _v) {
        //TODO Task 1.9
        Vector v = new Vector(doubElements);
        int roof = Math.max(v.getVectorSize(), _v.getVectorSize());
        double[] newArray = new double[roof];

        if (_v.getVectorSize() < v.getVectorSize()) {   //attempt to make 0 if statements here
            _v = _v.reSize(v.getVectorSize());
        }
        if (_v.getVectorSize() > v.getVectorSize()) {
            v = v.reSize(_v.getVectorSize());
        }
        double sum = 0;
        for (int i = 0; i < v.getVectorSize(); i++) {
            newArray[i] = v.getElementatIndex(i) * _v.getElementatIndex(i);
            sum = sum + newArray[i];
        }

        return sum;
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 1.10
        Vector v = new Vector(doubElements);
        int roof = Math.max(v.getVectorSize(), _v.getVectorSize());
        double[] newArray = new double[roof];

        if (_v.getVectorSize() < v.getVectorSize()) {   //attempt to make 0 if statements here
            _v = _v.reSize(v.getVectorSize());
        }
        if (_v.getVectorSize() > v.getVectorSize()) {
            v = v.reSize(_v.getVectorSize());
        }
        double sum1 = 0;
        for (int i = 0; i < v.getVectorSize(); i++) {
            newArray[i] = Math.pow(v.getElementatIndex(i), 2);
            sum1 = sum1 + newArray[i];
        }
        double sum2 = 0;
        for (int i = 0; i < _v.getVectorSize(); i++) {
            newArray[i] = Math.pow(_v.getElementatIndex(i), 2);
            sum2 = sum2 + newArray[i];
        }
        double finalSum = v.dotProduct(_v) / (Math.sqrt(sum1) * Math.sqrt(sum2));
        return finalSum;
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;
        //TODO Task 1.11
        if (v.getVectorSize() != this.getVectorSize()) {
            return false;
        }
        for (int i = 0; i < doubElements.length; i++) {
            if (v.getElementatIndex(i) != this.getElementatIndex(i)) {
                return false;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}