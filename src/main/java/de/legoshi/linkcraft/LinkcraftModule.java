package de.legoshi.linkcraft;

import de.legoshi.linkcraft.database.DBManager;
import team.unnamed.inject.AbstractModule;

public class LinkcraftModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DBManager.class).toProvider(() -> new DBManager(Linkcraft.getInstance(), "lc_")).singleton();
    }

}

