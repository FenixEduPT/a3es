package org.fenixedu.a3es.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.a3es.domain.util.ExportDegreeProcessBean;
import org.fenixedu.a3es.ui.strategy.MigrationStrategy;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.core.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.FenixFramework;

@Service
public class AccreditationProcessMigrationService {

    @Autowired
    private MessageSource messageSource;

    public void exportCurricularUnitFilesToA3es(ExportDegreeProcessBean form) {
        process(Authenticate.getUser(), I18N.getLocale(),
                () -> MigrationStrategy.getStrategy(form).exportCurricularUnitFilesToA3es(form, messageSource));
    }

    public void exportTeacherUnitFilesToA3es(ExportDegreeProcessBean form) {
        process(Authenticate.getUser(), I18N.getLocale(),
                () -> MigrationStrategy.getStrategy(form).exportTeacherUnitFilesToA3es(form, messageSource));
    }

    private <T> void process(final User user, final Locale locale, final Supplier<List<String>> s) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                FenixFramework.atomic(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Authenticate.mock(user, "System Automation");
                            I18N.setLocale(locale);
                            List<String> result = new ArrayList<String>();
                            try {
                                result = s.get();
                            } catch (RuntimeException e) {
                                result.add(e.getMessage());
                            }
                            Message.fromSystem().to(user.groupOf()).subject("A3ES Migration Result")
                                    .textBody(StringUtils.join(result, '\n')).send();

                        } finally {
                            Authenticate.unmock();
                        }
                    }
                });
            }
        };
        thread.start();
    }

}
