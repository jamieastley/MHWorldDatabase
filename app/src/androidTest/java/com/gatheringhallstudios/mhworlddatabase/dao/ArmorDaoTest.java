package com.gatheringhallstudios.mhworlddatabase.dao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase;
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao;
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType;
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank;
import com.gatheringhallstudios.mhworlddatabase.data.views.Armor;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasicView;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import java9.util.stream.StreamSupport;

import static com.gatheringhallstudios.mhworlddatabase.TestUtils.getValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ArmorDaoTest {
    private static MHWDatabase db;
    private static ArmorDao dao;

    @BeforeClass
    public static void initDatabase() {
        // this is read only, so its ok to use the actual database
        Context ctx = InstrumentationRegistry.getTargetContext();
        db = MHWDatabase.getDatabase(ctx);
        dao = db.armorDao();
    }

    @AfterClass
    public static void closeDatabase() {
        db.close();
    }

    @Test
    public void Can_Query_ArmorList() throws Exception {
        List<ArmorBasicView> results = getValue(dao.loadList("en"));
        assertFalse("expected results", results.isEmpty());
    }

    @Test
    public void Can_Filter_ArmorList_Rarity() throws Exception {
        List<ArmorBasicView> results = getValue(dao.loadList("en", 3, 3));
        boolean allAre3 = StreamSupport.stream(results).allMatch((a) -> a.getRarity() == 3);
        assertTrue("All armor should be rarity 3", allAre3);
    }

    @Test
    public void Can_Query_Armor() throws Exception {
        int armorId = 489; // Zorah Hide Alpha
        Armor result = getValue(dao.loadArmor("en", armorId));

        assertEquals("expect name match", "Zorah Hide Alpha", result.name);
        assertEquals("expected type to match", ArmorType.CHEST, result.armor_type);
    }

    @Test
    public void Can_Query_Armor_Sets_List() throws Exception {
        List<ArmorSetView> armorSets = getValue(dao.loadArmorSets("en", Rank.HIGH));

        for(ArmorSetView armorSet : armorSets) {
            boolean isAllEmpty = true;
            if (armorSet.getHead_armor() != null) isAllEmpty = false;
            if (armorSet.getChest_armor() != null) isAllEmpty = false;
            if (armorSet.getArms_armor() != null) isAllEmpty = false;
            if (armorSet.getWaist_armor() != null) isAllEmpty = false;
            if (armorSet.getLegs_armor() != null) isAllEmpty = false;

            assertFalse("expected non-empty armor", isAllEmpty);
        }

        assertFalse("expect list to be populated", armorSets.isEmpty());
    }
}
