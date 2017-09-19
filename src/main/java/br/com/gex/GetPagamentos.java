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
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jjunior
 */
public class GetPagamentos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {

            Connection conn = new ConectaMysql().conecta();
            
            String query = "SELECT "
                    + "    C.CRED_DS_CREDOR \"Credor\","
                    + "    INSCRICAO \"Inscrição\","
                    + "    PESSOA \"Pessoa\","
                    + "    NEGOCIACAO \"Negociação\","
                    + "    NUMERO \"Número\","
                    + "    PARCELA \"Parcela\","
                    + "    VALOR_NEGOCIADO \"Valor negociado\","
                    + "    PANN_NR_NOSSO_NUMERO \"Nosso número\","
                    + "    GUIA_DT_EMISSAO \"Data de emissão\","
                    + "    VALOR_PARCELA \"Valor da parcela\","
                    + "    HONORARIO \"Honorários\","
                    + "    DESPESAS \"Despesas\","
                    + "    CUSTAS \"Custas\","
                    + "    GUIA_DT_VENCIMENTO \"Data de vencimento\","
                    + "    PAPG_DT_PAGAMENTO \"Data de pagamento\","
                    + "    PAPG_VL_PAGO_TOTAL \"Valor do pagamento\""
                    + " FROM"
                    + "    giex.BI_PAGAMENTOS P"
                    + "    INNER JOIN giex.BI_CREDORES C ON P.CRED_ID_CREDOR = C.CRED_ID_CREDOR";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

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
