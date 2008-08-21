package de.fu_berlin.inf.gmanda.qda;

import junit.framework.TestCase;

import org.junit.Test;

public class PrimaryDocumentTest extends TestCase {
	
	@Test
	public void testGuessFromFileName(){
		assertEquals("gmane.comp.emulators.kvm.devel", PrimaryDocument.guessListFromFileName("mbox:D:\\svn\\sci\\mail.ti\\data\\comp.emulators.kvm.devel\\comp.emulators.kvm.devel.mbox"));
	}

}
