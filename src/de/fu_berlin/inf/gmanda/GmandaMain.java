/**
 * Copyright 2008 Christopher Oezbek
 * 
 * This file is part of GmanDA.
 *
 * GmanDA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GmanDA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GmanDA.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fu_berlin.inf.gmanda;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.AnnotatedFieldInjection;
import org.picocontainer.injectors.CompositeInjection;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.AbstractInjector.UnsatisfiableDependenciesException;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.gui.CodeAsTextView;
import de.fu_berlin.inf.gmanda.gui.CodeBox;
import de.fu_berlin.inf.gmanda.gui.CodeBoxView;
import de.fu_berlin.inf.gmanda.gui.CodeDetailBox;
import de.fu_berlin.inf.gmanda.gui.CodeDetailView;
import de.fu_berlin.inf.gmanda.gui.CodeList;
import de.fu_berlin.inf.gmanda.gui.CodeListFilterTextBox;
import de.fu_berlin.inf.gmanda.gui.CodeListView;
import de.fu_berlin.inf.gmanda.gui.CodingView;
import de.fu_berlin.inf.gmanda.gui.MainFrame;
import de.fu_berlin.inf.gmanda.gui.MetadataList;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeFilterTextField;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeView;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentView;
import de.fu_berlin.inf.gmanda.gui.TextView;
import de.fu_berlin.inf.gmanda.gui.actions.ApplicationScreenshotAction;
import de.fu_berlin.inf.gmanda.gui.actions.BackSelectionAction;
import de.fu_berlin.inf.gmanda.gui.actions.BasicStatisticalReportAction;
import de.fu_berlin.inf.gmanda.gui.actions.CloseAction;
import de.fu_berlin.inf.gmanda.gui.actions.ComputeEmailStatisticsAction;
import de.fu_berlin.inf.gmanda.gui.actions.ComputeThreadStatisticsAction;
import de.fu_berlin.inf.gmanda.gui.actions.CopyLinksAction;
import de.fu_berlin.inf.gmanda.gui.actions.CreateDSVFileAction;
import de.fu_berlin.inf.gmanda.gui.actions.DeletePrimaryDocumentAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExecuteTrailAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExitAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExportCitationsToLatexAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExportMessageIDHyperLinks;
import de.fu_berlin.inf.gmanda.gui.actions.FetchGmaneListToFileAction;
import de.fu_berlin.inf.gmanda.gui.actions.FormatCodeAction;
import de.fu_berlin.inf.gmanda.gui.actions.ForwardSelectionAction;
import de.fu_berlin.inf.gmanda.gui.actions.FullscreenTextViewAction;
import de.fu_berlin.inf.gmanda.gui.actions.ImportFromMboxAction;
import de.fu_berlin.inf.gmanda.gui.actions.ImportFromProjectAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertDateAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertSessionLogTemplateAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertSubCodeTemplateAction;
import de.fu_berlin.inf.gmanda.gui.actions.JumpToIdAction;
import de.fu_berlin.inf.gmanda.gui.actions.LoadAction;
import de.fu_berlin.inf.gmanda.gui.actions.LoadGmaneListAction;
import de.fu_berlin.inf.gmanda.gui.actions.LoadPrimaryDocumentAction;
import de.fu_berlin.inf.gmanda.gui.actions.MakeAllAvailableAction;
import de.fu_berlin.inf.gmanda.gui.actions.NewAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchListAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchRecursiveAction;
import de.fu_berlin.inf.gmanda.gui.actions.ReindexWithLuceneAction;
import de.fu_berlin.inf.gmanda.gui.actions.ReindexWithLuceneTreeAction;
import de.fu_berlin.inf.gmanda.gui.actions.RenameCodesAction;
import de.fu_berlin.inf.gmanda.gui.actions.ResetFilterAction;
import de.fu_berlin.inf.gmanda.gui.actions.RewindSelectionAction;
import de.fu_berlin.inf.gmanda.gui.actions.SaveAction;
import de.fu_berlin.inf.gmanda.gui.actions.SaveAsAction;
import de.fu_berlin.inf.gmanda.gui.actions.SearchWithLuceneAction;
import de.fu_berlin.inf.gmanda.gui.actions.SetCacheLocationAction;
import de.fu_berlin.inf.gmanda.gui.actions.ShowPreferencesAction;
import de.fu_berlin.inf.gmanda.gui.actions.SocialNetworkThreadAction;
import de.fu_berlin.inf.gmanda.gui.actions.SplitTrailAction;
import de.fu_berlin.inf.gmanda.gui.actions.VisualizationScreenshotAction;
import de.fu_berlin.inf.gmanda.gui.actions.tools.ExportSocialNetworksAction;
import de.fu_berlin.inf.gmanda.gui.docking.DefaultPerspective;
import de.fu_berlin.inf.gmanda.gui.docking.DockablePerspectiveMenu;
import de.fu_berlin.inf.gmanda.gui.docking.DockableViewMenu;
import de.fu_berlin.inf.gmanda.gui.docking.ViewManager;
import de.fu_berlin.inf.gmanda.gui.docking.VisualizationPerspective;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.manager.TitleManager;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.gui.menu.CodePopupMenu;
import de.fu_berlin.inf.gmanda.gui.menu.EditMenu;
import de.fu_berlin.inf.gmanda.gui.menu.FileMenu;
import de.fu_berlin.inf.gmanda.gui.menu.MainWindowMenuBar;
import de.fu_berlin.inf.gmanda.gui.menu.PrimaryDocumentMenu;
import de.fu_berlin.inf.gmanda.gui.menu.PrimaryDocumentTreePopup;
import de.fu_berlin.inf.gmanda.gui.menu.ToolsMenu;
import de.fu_berlin.inf.gmanda.gui.menu.WindowMenu;
import de.fu_berlin.inf.gmanda.gui.misc.DotFileFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.GmandaHyperlinkListener;
import de.fu_berlin.inf.gmanda.gui.misc.GmandaInfoBox;
import de.fu_berlin.inf.gmanda.gui.misc.LaTeXDirectoryChooser;
import de.fu_berlin.inf.gmanda.gui.misc.LockManager;
import de.fu_berlin.inf.gmanda.gui.misc.MBoxFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.PrimaryDocumentCellRenderer;
import de.fu_berlin.inf.gmanda.gui.misc.ProjectFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.SVGScreenshotTaker;
import de.fu_berlin.inf.gmanda.gui.misc.ScreenshotFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.StorageFileChooser;
import de.fu_berlin.inf.gmanda.gui.preferences.CacheDirectoryProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.CodedColorProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.DebugModePreferenceUI;
import de.fu_berlin.inf.gmanda.gui.preferences.DebugModeProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.GmandaPreferenceWindow;
import de.fu_berlin.inf.gmanda.gui.preferences.MatchColorProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.PrimaryDocumentDirectoryProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.ScrollOnShowPreferenceUI;
import de.fu_berlin.inf.gmanda.gui.preferences.ScrollOnShowProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.SeenColorProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.SelectedColorProperty;
import de.fu_berlin.inf.gmanda.gui.search.LuceneFacade;
import de.fu_berlin.inf.gmanda.gui.search.RepeatSearchAction;
import de.fu_berlin.inf.gmanda.gui.search.SearchField;
import de.fu_berlin.inf.gmanda.gui.search.SearchPanel;
import de.fu_berlin.inf.gmanda.gui.search.SearchService;
import de.fu_berlin.inf.gmanda.gui.tabulation.CrossTabulationView;
import de.fu_berlin.inf.gmanda.gui.tabulation.TabulationCanvas;
import de.fu_berlin.inf.gmanda.gui.tabulation.TabulationOptionsPanel;
import de.fu_berlin.inf.gmanda.gui.tree.CodeableControl;
import de.fu_berlin.inf.gmanda.gui.tree.CodeableTree;
import de.fu_berlin.inf.gmanda.gui.tree.CodeableTreeView;
import de.fu_berlin.inf.gmanda.gui.tree.JNodeCellRenderer;
import de.fu_berlin.inf.gmanda.gui.tree.JNodeSelectionListener;
import de.fu_berlin.inf.gmanda.gui.visualisation.ColorMapper;
import de.fu_berlin.inf.gmanda.gui.visualisation.TrackCompareManager;
import de.fu_berlin.inf.gmanda.gui.visualisation.VisualisationOptions;
import de.fu_berlin.inf.gmanda.gui.visualisation.VisualizationCanvas;
import de.fu_berlin.inf.gmanda.gui.visualisation.VisualizationPane;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.imports.MyXStream;
import de.fu_berlin.inf.gmanda.imports.PlainTextImporter;
import de.fu_berlin.inf.gmanda.imports.trail.TrailManager;
import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterKindProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SearchStringProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionViewManager;
import de.fu_berlin.inf.gmanda.proxies.StatusMessageProxy;
import de.fu_berlin.inf.gmanda.qda.ProjectLoader;
import de.fu_berlin.inf.gmanda.startup.CommandLineOptions;
import de.fu_berlin.inf.gmanda.startup.GUIInitializer;
import de.fu_berlin.inf.gmanda.startup.Initializer;
import de.fu_berlin.inf.gmanda.startup.LocaleInitializer;
import de.fu_berlin.inf.gmanda.startup.Starter;
import de.fu_berlin.inf.gmanda.startup.Stoppable;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.pico.DotGraphMonitor;
import de.fu_berlin.inf.gmanda.util.preferences.ColorProperties;
import de.fu_berlin.inf.gmanda.util.preferences.FileConverter;

public class GmandaMain {

	MutablePicoContainer container;
	
	public GmandaMain(CommandLineOptions cmd) {
		
		DotGraphMonitor dotMonitor = null;
		
		// Only start a DotGraphMonitor if asserts are enabled (aka debug mode)
		assert (dotMonitor = new DotGraphMonitor()) != null;
		
		initializeLogging();
		
		PicoBuilder picoBuilder = new PicoBuilder(new CompositeInjection(
				new ConstructorInjection(), new AnnotatedFieldInjection()))
				.withCaching().withLifecycle();

		/*
		 * If given, the dotMonitor is used to capture an architecture diagram
		 * of the application
		 */
		if (dotMonitor != null) {
			picoBuilder = picoBuilder.withMonitor(dotMonitor);
		}

		// Initialize our dependency injection container
		container = picoBuilder.build();
		
		container
			// Register the command line
			.addComponent(CommandLineOptions.class, cmd)
			// Register the application itself
			.addComponent(GmandaMain.class, this)
			// Register Initializers
			.addComponent(GUIInitializer.class)
			.addComponent(LocaleInitializer.class)
			// Proxies
			.addComponent(ProjectProxy.class)
			.addComponent(SelectionProxy.class)
			.addComponent(FilterProxy.class)
			.addComponent(StatusMessageProxy.class)
			.addComponent(SearchStringProxy.class)
			.addComponent(ForegroundWindowProxy.class)
			.addComponent(FilterTextProxy.class)
			.addComponent(FilterKindProxy.class)
			.addComponent(CodeDetailProxy.class)
			// Core Classes
			.addComponent(MainFrame.class)
			.addComponent(UndoManagement.class)
			.addComponent(LockManager.class)
			.addComponent(Configuration.class)
			// GUI Elements
			.addComponent(TextView.class)
			.addComponent(PrimaryDocumentTree.class)
			.addComponent(CodeBoxView.class)
			.addComponent(CodeBox.class)
			.addComponent(MetadataList.class)
			.addComponent(CodeList.class)
			.addComponent(SearchField.class)
			.addComponent(SearchPanel.class)
			.addComponent(PrimaryDocumentTreeFilterTextField.class)
			.addComponent(CodeListFilterTextBox.class)
			.addComponent(VisualisationOptions.class)
			.addComponent(VisualizationCanvas.class)
			.addComponent(PrimaryDocumentCellRenderer.class)
			.addComponent(CodeableTreeView.class)
			.addComponent(CodeableControl.class)
			.addComponent(CodeableTree.class)
			.addComponent(JNodeCellRenderer.class)
			.addComponent(JNodeSelectionListener.class)
			.addComponent(CodeDetailBox.class)
			.addComponent(TabulationCanvas.class)
			.addComponent(TabulationOptionsPanel.class)
			.addComponent(GmandaInfoBox.class)
			// Views
			.addComponent(CodeAsTextView.class)
			.addComponent(CodeDetailView.class)
			.addComponent(CrossTabulationView.class)
			.addComponent(PrimaryDocumentView.class)
			.addComponent(PrimaryDocumentTreeView.class)
			.addComponent(CodeListView.class)
			.addComponent(CodingView.class)
			.addComponent(VisualizationPane.class)
			// Perspectives
			.addComponent(DefaultPerspective.class)
			.addComponent(VisualizationPerspective.class)
			// Actions
			.addComponent(CloseAction.class)
			.addComponent(ExitAction.class)
			.addComponent(LoadAction.class)
			.addComponent(LoadPrimaryDocumentAction.class)
			.addComponent(LoadGmaneListAction.class)
			.addComponent(NewAction.class)
			.addComponent(SaveAction.class)
			.addComponent(SaveAsAction.class)
			.addComponent(ResetFilterAction.class)
			.addComponent(RenameCodesAction.class)
			.addComponent(JumpToIdAction.class)
			.addComponent(FetchGmaneListToFileAction.class)
			.addComponent(SetCacheLocationAction.class)
			.addComponent(RefetchAction.class)
			.addComponent(CopyLinksAction.class)
			.addComponent(CreateDSVFileAction.class)
			.addComponent(RefetchRecursiveAction.class)
			.addComponent(RefetchListAction.class)
			.addComponent(RepeatSearchAction.class)
			.addComponent(DeletePrimaryDocumentAction.class)
			.addComponent(FullscreenTextViewAction.class)
			.addComponent(BasicStatisticalReportAction.class)
			.addComponent(SearchWithLuceneAction.class)
			.addComponent(ReindexWithLuceneAction.class)
			.addComponent(ShowPreferencesAction.class)
			.addComponent(BackSelectionAction.class)
			.addComponent(ForwardSelectionAction.class)
			.addComponent(MakeAllAvailableAction.class)
			.addComponent(ExecuteTrailAction.class)
			.addComponent(FormatCodeAction.class)
			.addComponent(InsertDateAction.class)
			.addComponent(SplitTrailAction.class)
			.addComponent(InsertSubCodeTemplateAction.class)
			.addComponent(InsertSessionLogTemplateAction.class)
			.addComponent(RewindSelectionAction.class)
			.addComponent(VisualizationScreenshotAction.class)
			.addComponent(ApplicationScreenshotAction.class)
			.addComponent(ExportCitationsToLatexAction.class)
			.addComponent(ComputeThreadStatisticsAction.class)
			.addComponent(ComputeEmailStatisticsAction.class)
			.addComponent(SocialNetworkThreadAction.class)
			.addComponent(ImportFromMboxAction.class)
			.addComponent(ImportFromProjectAction.class)
			.addComponent(ReindexWithLuceneTreeAction.class)
			.addComponent(ExportMessageIDHyperLinks.class)
			.addComponent(ExportSocialNetworksAction.class)
			// Menus
			.addComponent(MainWindowMenuBar.class)
			.addComponent(FileMenu.class)
			.addComponent(EditMenu.class)
			.addComponent(PrimaryDocumentMenu.class)
			.addComponent(ToolsMenu.class)
			.addComponent(CodePopupMenu.class)
			.addComponent(PrimaryDocumentTreePopup.class)
			.addComponent(WindowMenu.class)
			.addComponent(DockableViewMenu.class)
			.addComponent(DockablePerspectiveMenu.class)
			// Loading Support
			.addComponent(MyXStream.class)
			.addComponent(ProjectLoader.class)
			.addComponent(PlainTextImporter.class)
			.addComponent(GmaneImporter.class)
			.addComponent(ProjectFileChooser.class)
			.addComponent(StorageFileChooser.class)
			.addComponent(ScreenshotFileChooser.class)
			.addComponent(LaTeXDirectoryChooser.class)
			.addComponent(GmaneMboxFetcher.class)
			.addComponent(GmaneFacade.class)
			.addComponent(DotFileFileChooser.class)
			.addComponent(MBoxFileChooser.class)
			// Properties
			.addComponent(ScrollOnShowProperty.class)
			.addComponent(PrimaryDocumentDirectoryProperty.class)
			.addComponent(CacheDirectoryProperty.class)
			.addComponent(ColorProperties.class)
			.addComponent(SeenColorProperty.class)
			.addComponent(MatchColorProperty.class)
			.addComponent(SelectedColorProperty.class)
			.addComponent(CodedColorProperty.class)
			.addComponent(DebugModeProperty.class)
			// Preference UI
			.addComponent(GmandaPreferenceWindow.class)
			.addComponent(ScrollOnShowPreferenceUI.class)
			.addComponent(DebugModePreferenceUI.class)
			// Converters
			.addComponent(FileConverter.class)
			// Others
			.addComponent(TitleManager.class)
			.addComponent(Starter.class)
			.addComponent(CommonService.class)
			.addComponent(ViewManager.class)
			.addComponent(ColorMapper.class)
			.addComponent(TrackCompareManager.class)
			.addComponent(LuceneFacade.class)
			.addComponent(SearchService.class)
			.addComponent(SelectionViewManager.class)
			.addComponent(TrailManager.class)
			.addComponent(GmandaHyperlinkListener.class)
			.addComponent(SVGScreenshotTaker.class)
			.addComponent(VelocitySupport.class)
			.addComponent(SocialNetworkModule.class);
		
			if (dotMonitor != null){
				container.addComponent(DotGraphMonitor.class, dotMonitor);
			}
		
	}

	protected void initializeLogging() {
		try {
            PropertyConfigurator.configureAndWatch("log4j.properties",
                60 * 1000);
        } catch (SecurityException e) {
            LogLog.error("Could not start logging:", e);
        }
	}

	public void startApplication() {

		try {
			// Run Initializers first
			for (Initializer init : container.getComponents(Initializer.class)) {
				init.initialize();
			}

			// Then make sure that all other components get instantiated
			container.getComponents(Object.class);

			// Now start
			container.getComponent(Starter.class).start();
		} catch (UnsatisfiableDependenciesException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void stopApplication() {

		// Tell everybody that we are stopping!
		for (Stoppable s : container.getComponents(Stoppable.class)){
			s.stop();
		}

		System.exit(0);
	}

	public static void main(String[] args) {
		
		CommandLineOptions cmd = new CommandLineOptions();
		
		try {
		 	cmd.parse(args);
		} catch( ReportToUserException e ) {
			System.err.println(e.getErrorMessage());
			
			System.exit(0);
		}
		
		GmandaMain main = new GmandaMain(cmd);
		
		main.startApplication();
	}
}
