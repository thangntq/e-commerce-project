package com.shopme.admin.user.exporter;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse,"text/csv",".csv","user__");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID","E-mail","First Name", "Last Name", "Roles","Enabled"};

        String[]  fieldMapping = {"id","email","firstName","lastName","roles","enabled"};
        csvBeanWriter.writeHeader(csvHeader);

        for (User user : listUsers){
            csvBeanWriter.write(user,fieldMapping);
        }
        csvBeanWriter.close();
    }
}
