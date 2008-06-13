package de.fu_berlin.inf.gmanda.imports;

import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentDataV1;
import de.fu_berlin.inf.gmanda.qda.ProjectData;
import de.fu_berlin.inf.gmanda.qda.ProjectDataV1;
import de.fu_berlin.inf.gmanda.qda.ProjectDataV2;

public class FileVersionConvert {

	public static ProjectData fromV1(ProjectDataV1 v1) {
		ProjectDataV2 result = new ProjectDataV2();
		result.rootDocuments = v1.rootDocuments;

		return fromV2(result);
	}

	public static ProjectData fromV2(ProjectDataV2 v2) {
		ProjectData result = new ProjectData();
		result.rootDocuments = PrimaryDocumentDataV1.convert(v2.rootDocuments);

		return result;
	}

}
