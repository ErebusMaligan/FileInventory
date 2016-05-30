package db;

import java.sql.SQLException;

import cache.DataSourceProxy;
import datasource.DBSource;
import db.instance.DatabaseInstance;
import db.instance.generic.wrapper.TableWrapper;
import db.instance.specific.sqlite.SQLiteDatabase;
import db.table.TableFactory;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Feb 28, 2014, 11:08:12 PM 
 */
public class DataStore {
	
	private static DataStore instance;
	
	private DataStore() {}
	
	public static DataStore getInstance() {
		if ( instance == null ) {
			instance = new DataStore();
		}
		return instance;
	}
	
	public void initDB( String dbFile ) {
		DatabaseInstance dbi = new SQLiteDatabase();
		Database.create( dbi ).getDatabaseWrapper().setName( dbFile );
		DBSource db = new DBSource();
		db.createDatabase( dbi );
		DataSourceProxy.getInstance().setDataSource( db );
		try {
			create( false );
		} catch ( ClassNotFoundException | SQLException e ) {
			e.printStackTrace();
		}
//		db.startScanner( false );
//		db.getScanner().startPolling( 180000 );
	}
	
	public void printData() {
		DataSourceProxy.getInstance().getCache().printCache();
	}
	
	public void create( boolean drop ) throws ClassNotFoundException, SQLException {
		( (DBSource)DataSourceProxy.getInstance().getDataSource() ).stopScanner();
		DataSourceProxy.getInstance().getCache().clear();
		for ( String s : DBN.ALL_TABLES ) {
			try {
				TableWrapper.create( TableFactory.getTable( s ), drop, Database.getInstance().getDatabaseWrapper().getConnection() );
			} catch ( SQLException e ) {
				//e.printStackTrace();  mostly this happens if table already exists, which will almost always happen.. so don't print anything
			}
		}
		( (DBSource)DataSourceProxy.getInstance().getDataSource() ).startScanner( true );
		( (DBSource)DataSourceProxy.getInstance().getDataSource() ).getScanner().startPolling( 180000 );
	}
}