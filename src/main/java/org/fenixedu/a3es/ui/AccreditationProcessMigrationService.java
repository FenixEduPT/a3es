package org.fenixedu.a3es.ui;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.a3es.domain.util.ExportDegreeProcessBean;
import org.fenixedu.a3es.ui.strategy.MigrationStrategy;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.FenixFramework;

@Service
public class AccreditationProcessMigrationService {

    @Autowired
    private MessageSource messageSource;

    public void exportCurricularUnitFilesToA3es(ExportDegreeProcessBean form) {
        process(Authenticate.getUser(), I18N.getLocale(), () -> MigrationStrategy.getStrategy(form).exportCurricularUnitFilesToA3es(form, messageSource));
    }

    public void exportTeacherUnitFilesToA3es(ExportDegreeProcessBean form) {
        process(Authenticate.getUser(), I18N.getLocale(), () -> MigrationStrategy.getStrategy(form).exportTeacherUnitFilesToA3es(form, messageSource));
    }

    private <T> void process(final User user, final Locale locale, final Supplier<List<String>> s) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                FenixFramework.atomic(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Authenticate.mock(user);
                            I18N.setLocale(locale);
                            final List<String> result = s.get();
                            final SystemSender sender = Bennu.getInstance().getSystemSender();
                            final Recipient recipient = new Recipient(Collections.singleton(user.getPerson()));
                            new Message(sender, recipient, "A3ES Migration Result", StringUtils.join(result, '\n'));
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
