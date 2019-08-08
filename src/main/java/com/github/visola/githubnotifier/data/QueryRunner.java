package com.github.visola.githubnotifier.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class QueryRunner {

  private final DataSource dataSource;

  @Autowired
  public QueryRunner(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void run() {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      try (Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
        System.out.print("> ");
        String query = in.readLine();
        if (!Strings.isNullOrEmpty(query)) {
          if (st.execute(query)) {
            ResultSet rs = st.getResultSet();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
              System.out.printf("%s | ", rsmd.getColumnName(i));
            }
            System.out.print("\n---\n");
            while (rs.next()) {
              for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.printf("%s | ", rs.getString(rsmd.getColumnName(i)));
              }
              System.out.println();
            }
            System.out.print("\n---\n");
          } else {
            System.out.printf("%d rows updated.%n", st.getUpdateCount());
          }
        }
      } catch (IOException | SQLException e) {
        e.printStackTrace();
      }
    }
  }

}
