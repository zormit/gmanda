package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import de.fu_berlin.inf.gmanda.gui.actions.BasicStatisticalReportAction;
import de.fu_berlin.inf.gmanda.gui.actions.CreateDSVFileAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExecuteTrailAction;
import de.fu_berlin.inf.gmanda.gui.actions.FetchGmaneListToFileAction;
import de.fu_berlin.inf.gmanda.gui.actions.ReindexWithLuceneAction;
import de.fu_berlin.inf.gmanda.gui.actions.SearchWithLuceneAction;
import de.fu_berlin.inf.gmanda.gui.actions.SetCacheLocationAction;
import de.fu_berlin.inf.gmanda.gui.actions.SplitTrailAction;

public class ToolsMenu extends JMenu {

	public ToolsMenu(FetchGmaneListToFileAction fetchAction,
		SetCacheLocationAction setPDLocationAction, CreateDSVFileAction createDSVAction,
		SearchWithLuceneAction searchLucene, ReindexWithLuceneAction reindexLucene,
		BasicStatisticalReportAction stats, ExecuteTrailAction executeTrail,
		SplitTrailAction splitTrail) {

		super("Tools");
		add(fetchAction);
		add(setPDLocationAction);
		add(createDSVAction);
		
		add(new JSeparator());
		
		add(searchLucene);
		add(reindexLucene);
		
		add(new JSeparator());
		
		add(stats);
		add(executeTrail);
		add(splitTrail);
	}

}
