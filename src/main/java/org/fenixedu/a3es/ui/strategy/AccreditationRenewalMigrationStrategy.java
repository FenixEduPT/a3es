package org.fenixedu.a3es.ui.strategy;

public class AccreditationRenewalMigrationStrategy extends MigrationStrategy {

    @Override
    protected String getProcessFolderName() {
        return "Apresentação do pedido";
    }

    @Override
    protected String getCompetenceCoursesFolderIndex() {
        return "3.2.";
    }

    @Override
    protected String getCompetenceCoursesFolderName() {
        return getCompetenceCoursesFolderIndex() + " Organização das Unidades Curriculares";
    }
}
