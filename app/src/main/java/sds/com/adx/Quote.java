package sds.com.adx;

final class Quote {
    private int id;
	private String symbol;
    private String date;
    private double high;
    private double low;
    private double close;
    private double tr;
    private double dm1_plus;
    private double dm1_minus;
    private double tr14;
    private double dm14_plus;
    private double dm14_minus;
    private double di14_plus;
    private double di14_minus;
    private double diff;
    private double sum;
    private double dx;
    private double adx;
    private double twelveHigh;
    
    
    public Quote(int id, String symbol, String date, double high, double low, double close, double adx, double tr14, double dm14_plus, double dm14_minus, double twelveHigh) {
    	this.id = id;
        this.symbol = symbol;
        this.date = date;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adx = adx;
        this.tr14 = tr14;
        this.dm14_plus = dm14_plus;
        this.dm14_minus = dm14_minus;
        this.twelveHigh = twelveHigh;
        
    }

 

    public int getID() {
        return id;
    }
    
    public void setID(int id) {
    	this.id = id;
    }
    
    public String getDIRECTION() {
        return symbol;
    }
    
    public void setDirection(String direction) {
    }
    
    public String getSYMBOL() {
        return symbol;
    }
    
    public void setSYMBOL(String symbol) {
    	this.symbol = symbol;
    }
    
    public String getDATE() {
        return date;
    }

    public void setDATE(String date) {
    	this.date = date;
    }
    
    public double getHIGH() {
        return high;
    }

    public void setHIGH(double high) {
    	this.high = high;
    }
    
    public double getLOW() {
        return low;
    }

    public void setLOW(double low) {
    	this.low = low;
    }
    
    public double getCLOSE() {
        return close;
    }

    public void setCLOSE(double close) {
    	this.close = close;
    }
    
    public double getTR() {
        return tr;
    }

    public void setTR(double tr) {
    	this.tr = tr;
    }
    
    public double getDM1_PLUS() {
        return dm1_plus;
    }
    
    public void setDM1_PLUS(double dm1_plus) {
    	this.dm1_plus = dm1_plus;
    }
    
    public double getDM1_MINUS() {
        return dm1_minus;
    }
    
    public void setDM1_MINUS(double dm1_minus) {
    	this.dm1_minus = dm1_minus;
    }
    
    public double getTR14() {
        return tr14;
    }
    
    public void setTR14(double tr14) {
    	this.tr14 = tr14;
    }
    
    public double getDM14_MINUS() {
        return dm14_minus;
    }
    
    public void setDM14_MINUS(double dm14_minus) {
    	this.dm14_minus = dm14_minus;
    }
    
    public double getDM14_PLUS() {
        return dm14_plus;
    }
    
    public void setDM14_PLUS(double dm14_plus) {
    	this.dm14_plus = dm14_plus;
    }
    
    public double getDI14_MINUS() {
        return di14_minus;
    }
    
    public void setDI14_MINUS(double di14_minus) {
    	this.di14_minus = di14_minus;
    }
    
    public double getDI14_PLUS() {
        return di14_plus;
    }
    
    public void setDI14_PLUS(double di14_plus) {
    	this.di14_plus = di14_plus;
    }
   
    public double getDIFF() {
        return diff;
    }
    
    public void setDIFF(double diff) {
    	this.diff = diff;
    }

    public double getSUM() {
        return sum;
    }
    
    public void setSUM(double sum) {
    	this.sum = sum;
    }
    
    public void setDX(double dx) {
    	this.dx = dx;
    }
    
    public double getDX() {
        return dx;
    }
    
    public void setADX(double adx) {
    	this.adx = adx;
    }
    
    public double getADX() {
        return adx;
    }
   
    public void setTwelveHigh(double twelveHigh) {
    	this.twelveHigh = twelveHigh;
    }
    
    public double getTwelveHigh() {
        return this.twelveHigh;
    }
}
