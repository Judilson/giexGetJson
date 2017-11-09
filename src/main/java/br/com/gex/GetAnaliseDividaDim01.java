/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gex;

import br.com.giex.conexao.ConectaMysql;

import com.flexmonster.compressor.Compressor;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jjunior
 */
public class GetAnaliseDividaDim01 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {

            Connection conn = new ConectaMysql().conecta();
            PreparedStatement psmt = null;

            String query = "select *"
                    + "  FROM   giex.TB_ANALISE_DIVIDAS_DIM01"
                    + " WHERE   fase_sg_fase = 'N'";

            psmt = conn.prepareStatement(query);
            //psmt.setInt(1, Integer.parseInt(request.getParameter("idCredor")));

            ResultSet resultSet = psmt.executeQuery();
            
            InputStream inputStream = Compressor.compressDb(resultSet);
            
            int available = inputStream.available();

            response.setContentType("text/plain");
            OutputStream outputStream = response.getOutputStream();
            int length = 0;
            byte[] buffer = new byte[available/2];
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }

//            ResultSet resultSet = psmt.executeQuery();
//
//            response.setContentType("text/html; charset=UTF-8");
//            response.setContentType("application/json");
//
//            JSONArray jsonArray = new JSONArray();
//            while (resultSet.next()) {
//                int total_rows = resultSet.getMetaData().getColumnCount();
//                JSONObject obj = new JSONObject();
//
//                for (int i = 1; i <= total_rows; i++) {
//
//                    if (resultSet.getObject(i) == null) {
//                        obj.put(resultSet.getMetaData().getColumnLabel(i), JSONObject.NULL);
//                    } else {
//                        obj.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i));
//                    }
//                }
//                jsonArray.put(obj);
//            }
//
//            OutputStream os = response.getOutputStream();
//            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
//            jsonArray.write(osw);
//            osw.flush();
//            osw.close();
//            os.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
