package de.fu_berlin.inf.gmanda.imports;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentDataV1;
import de.fu_berlin.inf.gmanda.qda.ProjectData;
import de.fu_berlin.inf.gmanda.qda.ProjectDataV1;
import de.fu_berlin.inf.gmanda.qda.ProjectDataV2;
import de.fu_berlin.inf.gmanda.qda.TagComma.TagCommaFactory;
import de.fu_berlin.inf.gmanda.qda.TagComma.TagCommaFactory.TagCommaString;
import de.fu_berlin.inf.gmanda.qda.tagxon.TagxON;
import de.fu_berlin.inf.gmanda.util.CStringUtils;
import de.fu_berlin.inf.gmanda.util.CStringUtils.StringConverter;
import de.fu_berlin.inf.gmanda.util.tree.ChildrenableTreeWalker;

public class FileVersionConvert {

	public static ProjectData fromV1(ProjectDataV1 v1) {
		ProjectDataV2 result = new ProjectDataV2();
		result.rootDocuments = v1.rootDocuments;

		return fromV2(result);
	}

	public static ProjectData fromV2(ProjectDataV2 v2) {
		ProjectData result = new ProjectData();
		result.rootDocuments = PrimaryDocumentDataV1.convert(v2.rootDocuments);

		return fromV3(result);
	}
	
	public static ProjectData fromV3(ProjectData v3) {
		
		/**
		 * Convert from TagCommaString to TagxON-Codes
		 */
		
		Iterable<PrimaryDocumentData> allDocuments = new ChildrenableTreeWalker<PrimaryDocumentData>(
			v3.rootDocuments);
		
		TagxON tagxON = new TagxON();
		
		TagCommaFactory tagSyntax = new TagCommaFactory();
		
		
		for (PrimaryDocumentData pd : allDocuments){
			
			String codeString = StringEscapeUtils.unescapeJava(pd.code);
			
			TagCommaString s = new TagCommaString(codeString);
			
			String newCode = CStringUtils.join(s.getAllCodes(), ",\n", new StringConverter<Code>(){

				public String toString(Code code) {
 
					StringBuilder sb = new StringBuilder();
					
					sb.append(code.toString(false, true));
					
					List<? extends Code> props = code.getProperties();
					
					if (props.size() > 0){
						sb.append(": ");
						
						if (props.size() == 1 && props.get(0).getTag().equals("desc")){
							sb.append(props.get(0).getValue());
						} else {
							sb.append("{ ");
							sb.append(CStringUtils.join(props, ", ", new StringConverter<Code>(){

								public String toString(Code t) {
									StringBuilder sb = new StringBuilder();
									
									sb.append(t.getTag());
									
									if (t.getValue() != null){
										sb.append(": ");
										sb.append(t.getValue());
									}
									return sb.toString();
								}
							}));
							sb.append(" }");
						}
						
					}
					
					return sb.toString();
				}
			});
			
			pd.code = StringEscapeUtils.escapeJava(tagxON.parseCodedString(newCode).format());
		
		}
		
		return v3;
	}

}
