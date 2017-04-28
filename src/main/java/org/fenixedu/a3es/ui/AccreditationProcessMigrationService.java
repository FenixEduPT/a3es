package org.fenixedu.a3es.ui;

import java.util.List;

import org.fenixedu.a3es.domain.util.ExportDegreeProcessBean;
import org.fenixedu.a3es.ui.strategy.MigrationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class AccreditationProcessMigrationService {

    @Autowired
    private MessageSource messageSource;

    public List<String> exportCurricularUnitFilesToA3es(ExportDegreeProcessBean form) {
        return MigrationStrategy.getStrategy(form).exportCurricularUnitFilesToA3es(form, messageSource);
    }

    public List<String> exportTeacherUnitFilesToA3es(ExportDegreeProcessBean form) {
        return MigrationStrategy.getStrategy(form).exportTeacherUnitFilesToA3es(form, messageSource);
    }

}
