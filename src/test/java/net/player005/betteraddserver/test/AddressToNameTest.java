package net.player005.betteraddserver.test;

import net.player005.betteraddserver.AddressToName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AddressToNameTest {

    private void assertConversionEquals(String address, String assertedName) {
        var actualName = AddressToName.toName(address);
        Assertions.assertEquals(assertedName, actualName);
    }

    @Test
    public void standardServerNameTest() {
        assertConversionEquals("hypixel.net", "Hypixel");
    }

    @Test
    public void stripPlayPrefix() {
        assertConversionEquals("play.server.de", "Server");
        assertConversionEquals("mc.hypixel.net", "Hypixel");
    }

    @Test
    public void keepCapitalisation() {
        assertConversionEquals("abcServerXYZ.com", "AbcServerXYZ");
    }

    @Test
    public void capitaliseAbbreviations() {
        assertConversionEquals("mcplayhd.net", "MCPlayHD");
    }

    @Test
    public void capitaliseShortSubdomains() {
        assertConversionEquals("eu.server.net", "EU Server");
    }

    @Test public void keepSpecialDomainEndings() {
        assertConversionEquals("scrims.network", "Scrims Network");
        assertConversionEquals("eu.mcpvp.club", "EU MCPVP Club");
    }
}
