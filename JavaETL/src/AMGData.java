/**
 * This class helps manage the database portion of the ETL utility
 */
public class AMGData{
	private String basePath = "";

	public AMGData(){
	}
	
	public AMGData(String base){
		basePath = base;
	}
	
	public void createDB(){
		if(createBaseballDB()){
		}
		else{
			System.out.println("Error creating baseball db...");
		}
		if(createStockDB()){
		}
        else{
            System.out.println("Error creating stock db...");
        }
	}

	private boolean createBaseballDB(){
        try{
			String longSQL = "create table if not exists baseball_stats(player_name text primary";
			longSQL += "key,games_played integer default '0',average real default '0.00',salary real";
			longSQL += "default '0.00')";

			AMGSQLite dbSession = new AMGSQLite(basePath + "/db/baseball.db");
			if(!dbSession.runQuery(longSQL)){
				return false;
			}
			dbSession.runQuery("delete from baseball_stats");
        }
        catch(Exception e){
            return false;
        }
        return true;
	}

	private boolean createStockDB(){
		try{
			String longSQL = "create table if not exists stock_stats(company_name text,ticker text";
        	longSQL += "primary key,country text,price real default '0.00',exchange_rate real default '0.00',";
        	longSQL += "shares_outstanding real default '0.00',net_income real default";
        	longSQL += "'0.00',market_value real default '0.00',pe_ratio real default '0.00')";

            AMGSQLite dbSession = new AMGSQLite(basePath + "/db/stocks.db");
            if(!dbSession.runQuery(longSQL)){
				System.out.println("Failed to add record...");
                return false;
            }
			dbSession.runQuery("delete from stock_stats");
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	public boolean insertBaseball(BaseballStatRecord a){
		try{
			String longSQL = "insert into baseball_stats (player_name,games_played, average, salary)";
			longSQL += String.format(" values ('%s','%d','%f','%f')", a.getName(),a.getGames(),a.getAverage(),a.getSalary());
			AMGSQLite dbSession = new AMGSQLite(basePath + "/db/baseball.db");
            if(!dbSession.runQuery(longSQL)){
                return false;
            }
		}
		catch(Exception e){
			return false;
		}
		return true;	
	}

    public boolean insertStock(StockStatRecord a){
        try{
            String longSQL = "insert into stock_stats (company_name,ticker,country,price,exchange_rate,shares_outstanding,net_income, market_value,pe_ratio)";
            longSQL += String.format(" values ('%s','%s','%s','%f','%f','%f','%f','%f','%f')", a.getCompanyName(),a.getName(),
							a.getExchangeCountry(),a.getPrice(),a.getExchangeRate(),
							a.getSharesOutstanding(),a.getNetIncome(),a.getMarketValueUsd(),a.getPERatio());
            AMGSQLite dbSession = new AMGSQLite(basePath + "/db/stocks.db");
            if(!dbSession.runQuery(longSQL)){
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
}
