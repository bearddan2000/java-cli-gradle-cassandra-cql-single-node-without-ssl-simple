package example.dto;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.*;
import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;

import com.datastax.oss.driver.api.core.cql.ResultSet;

import java.util.*;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.driver.core.DataType;
import com.datastax.oss.driver.api.core.cql.ColumnDefinition;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;

public class Generic {
  private static final Logger logger = Logger.getLogger(Generic.class);

	private CqlSession session;
  private String PWD;

  public Generic(String node, String pwd)
  {
    final String connectionMsg = " to cluster";
    try {
  		session = CqlSession.builder()
        .addContactPoint(new InetSocketAddress(node, 9042))
        .withLocalDatacenter("datacenter1")
        .build();
      logger.info("Connected" + connectionMsg);
    } catch(Exception e) {
      logger.error("Failed to connect" + connectionMsg);
      for (StackTraceElement st : e.getStackTrace())
        logger.error(st.toString());
      System.exit(1);
    }
    PWD = pwd;
  }

  private String readcqlFile(String file) throws IOException
  {
    // Now calling Files.readString() method to
    // read the file
    String fileName = PWD + "/src/main/resources/cql/" + file;
    // Now calling Files.readString() method to
    // read the file
    String str = new String(
      java.nio.file.Files.readAllBytes(
      java.nio.file.Paths.get(fileName)), java.nio.charset.StandardCharsets.UTF_8);

    str = str.trim().replace("\t", " ").replace("\r", " ").replace("\n", " ");

    return str;
  }

  private void getColumns(Row row, ColumnDefinitions columeDef)
  {
    for(ColumnDefinition def: columeDef){

      String name = def.getName().toString();
      String t = def.getType().toString();

      if(t.equals("INT"))
          logger.info(name+": "+row.getInt(name));
      else if(t.equals("TEXT"))
          logger.info(name+": "+row.getString(name));
      else
          logger.info(name+": "+def.getType().toString());
    }
  }

  private void selectColumnFamily(CqlSession session, String sql)
  {
    ResultSet results = session.execute(sql);
		results.forEach(row ->
			getColumns(row, row.getColumnDefinitions())
		);
  }
  public void operation(String fileName, CQLOPT opt)
  {
    try {
      String sql = readcqlFile(fileName+opt.cqlFile);
      if (opt == CQLOPT.SELECT)
        selectColumnFamily(session, sql);
      else
        session.execute(sql);
    } catch(IOException e) {
      logger.warn(String.format("Failed readcqlFile path: %s", PWD));
      logger.warn(String.format("Failed to readcqlFile: %s", fileName+opt.cqlFile));
    }
    logger.info(opt.operation+" complete");
  }

	public void close() {
		session.close();
	}
}
