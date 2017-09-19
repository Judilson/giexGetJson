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
public class GetRateios extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {

            Connection conn = new ConectaMysql().conecta();
            PreparedStatement psmt = null;

            String query = "SELECT "
                    + "    c.CRED_DS_CREDOR \"Credor\","
                    + "    RERA_DT_CREDITO \"Data de crédito\","
                    + "    RERA_VL_RECEBIDO \"Valor recebido\","
                    + "    RERA_VL_CREDITADO \"Valor creditado\","
                    + "    RERA_NM_FAVORECIDO \"Nome do favorecido\","
                    + "    RERA_NR_NOSSO_NUMERO \"Nosso número\","
                    + "    CONCAT('Banco ',"
                    + "            RERA_NR_BANCO,"
                    + "            '; Ag ',"
                    + "            RERA_NR_PREF_AG,"
                    + "            '-',"
                    + "            RERA_CD_AG_DIG,"
                    + "            '; CC ',"
                    + "            RERA_NR_CONTA,"
                    + "            '-',"
                    + "            RERA_CD_CC_DIG) \"Conta\""
                    + " FROM"
                    + "    BI_RATEIO r"
                    + "        INNER JOIN"
                    + "    BI_CREDORES c ON r.CRED_ID_CREDOR = c.CRED_ID_CREDOR"
                    + " WHERE"
                    + "    IFNULL(RERA_VL_PARTILHADO, 0) > 0"
                    + " AND r.CRED_ID_CREDOR=?";

            psmt = conn.prepareStatement(query);
            psmt.setInt(1, Integer.parseInt(request.getParameter("idCredor")));

            ResultSet resultSet = psmt.executeQuery();

            InputStream inputStream = Compressor.compressDb(resultSet);

            response.setContentType("text/plain");
            OutputStream outputStream = response.getOutputStream();
            int length = 0;
            byte[] buffer = new byte[10240];
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }

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
