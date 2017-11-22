/**
 * This class is specifically for a stock stat record
 */
public class StockStatRecord extends AbstractRecord{
	private String companyName = "";    
   	private String exchangeCountry = "";
    private float price = 0;
    private float exchangeRate = 0;  
    private float sharesOutstanding = 0;
    private float netIncome = 0;
    private float marketValueUsd = 0;
	private float peRatio = 0;

    public StockStatRecord(){
    }

	public StockStatRecord(String n, String cn, String c, float p, float r, float s, float n2){
		super(n);
		companyName = cn;
		exchangeCountry = c;
		price = p;
		exchangeRate = r;
		sharesOutstanding = s;
		netIncome = n2;
	}

	public void setCompanyName(String cn){
		companyName = cn;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setExchangeCountry(String c){
		exchangeCountry = c;
	}

	public String getExchangeCountry(){
		return exchangeCountry;
	}

	public void setPrice(float p){
		price = p;
	}

	public float getPrice(){
		return price;
	}

	public void setExchangeRate(float r){
		exchangeRate = r;
	}
	
	public float getExchangeRate(){
		return exchangeRate;
	}

	public void setSharesOutstanding(float s){
		sharesOutstanding = s;
	}

	public float getSharesOutstanding(){
		return sharesOutstanding;
	} 

	public void setNetIncome(float n){
		netIncome = n;
	}

	public float getNetIncome(){
		return netIncome;
	}

	public void setMarketValueUsd(float v){
		marketValueUsd = v;
	}

	public float getMarketValueUsd(){
		return marketValueUsd;
	}

	public void setPERatio(float r2){
		peRatio = r2;
	}

	public float getPERatio(){
		return peRatio;
	}
    @Override
    public String toString2(){
        return String.format("StockStatRecord(%s,%s,EC=%s,$Price=%.2f,$E/R=%.2f,S/E=%.2f,$Net=%.2f,$Cap=%.2f,P/E=%.2f)",
				name,companyName,exchangeCountry,price,exchangeRate,sharesOutstanding,netIncome,marketValueUsd, peRatio
		);
    }
}

