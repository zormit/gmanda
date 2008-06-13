package de.fu_berlin.inf.gmanda.qda;

import junit.framework.TestCase;

public class PrimaryDocumentTest extends TestCase {
	
	public void testGuessFromFileName(){
		assertEquals("comp.emulators.kvm.devel", PrimaryDocument.guessListFromFileName("mbox:D:\\svn\\sci\\mail.ti\\data\\comp.emulators.kvm.devel\\comp.emulators.kvm.devel.mbox"));
	}

}
