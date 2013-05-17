package org.agmip.translators.qckdl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.agmip.ace.AceDataset;
import org.agmip.ace.AceExperiment;
import org.agmip.ace.io.AceParser;
import org.agmip.ace.lookup.LookupCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AgMIP quick download information file translation Class
 *
 * @author Meng Zhang
 * @version 1.0
 */
public class QckdlMetaOutput {

    private static final Logger LOG = LoggerFactory.getLogger(QckdlMetaOutput.class);

    /**
     * DSSAT Observation Data Output method
     *
     * @param out The output stream for output contents
     * @param in The input stream contain data set
     */
    public void writeFile(OutputStream out, InputStream in) throws IOException {
        
        loadTitle(out);
        AceDataset data = AceParser.parse(in);
        StringBuilder sb = new StringBuilder();

        for (AceExperiment exp : data.getExperiments()) {

            String agmipDate = exp.getValueOr("agmip_date", "");
            String agmipRater = exp.getValueOr("agmip_rater", "");
            String exDistrib = exp.getValueOr("ex_distrib", "");
            String wstDistrib = exp.getValueOr("wst_distrib", "");
            boolean isUnrated = agmipDate.equals("") || agmipRater.equals("");
            boolean isExpDistrib = exDistrib.equals("Y");
            boolean isWstDistrib = wstDistrib.equals("Y");

            sb.append(escapeCsvStr(exp.getValueOr("exname", ""))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("trt_name", ""))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("institution", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("wst_id", ""))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("tavp", ""), isWstDistrib, "contact " + exp.getValueOr("wst_email", "N/A") + " for weather dataset")).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("prcp", ""), isWstDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("soil_id", ""))).append(",");
            sb.append(escapeCsvStr(LookupCodes.lookupCode("sltx", exp.getValueOr("sltx", ""), "soil_texture"))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("data_source", ""))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("agmip_rating", ""), isUnrated, "Unrated")).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("fl_lat", ""))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("fl_long", ""))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("flele", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("local_name", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("main_factor", ""))).append(",");
            sb.append(escapeCsvStr(LookupCodes.lookupCode("crid", exp.getValueOr("crid", ""), "common"))).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("cul_name", ""), isExpDistrib, "contact " + exp.getValueOr("ex_email", "N/A") + " for dataset")).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("pdate", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("adat", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("mdat", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("hdate", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("hwah", exp.getValueOr("hwam", "")), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("cwah", exp.getValueOr("cwam", "")), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("ir#c", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("ir_tot", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("fe_#", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("fen_tot", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("fep_tot", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("fek_tot", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("om_tot", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("obs_end_of_season", ""), isExpDistrib)).append(",");
            sb.append(escapeCsvStr(exp.getValueOr("obs_time_series_data", ""), isExpDistrib)).append("\r\n");
        }
        out.write(sb.toString().getBytes());
    }

    private void loadTitle(OutputStream out) throws IOException {
        InputStream titleIn = getClass().getClassLoader().getResourceAsStream("Metadata_title.csv");
        int count;
        byte[] data = new byte[1024];
        while ((count = titleIn.read(data)) != -1) {
            out.write(data, 0, count);
        }
    }

    private String escapeCsvStr(String str, boolean isRestricted) {
        return escapeCsvStr(str, isRestricted, "");
    }

    private String escapeCsvStr(String str, boolean isRestricted, String altVal) {
        if (isRestricted) {
            return escapeCsvStr(str);
        } else {
            return escapeCsvStr(altVal);
        }
    }

    private String escapeCsvStr(String str) {
        if (str != null || str.equals("")) {
            boolean needQuote = false;
            if (str.contains("\"")) {
                str = str.replaceAll("\"", "\"\"");
                needQuote = true;
            }
            if (!needQuote && str.contains(",")) {
                needQuote = true;
            }
            if (needQuote) {
                str = "\"" + str + "\"";
            }
            return str;
        } else {
            return "";
        }
    }
}
