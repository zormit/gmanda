package de.fu_berlin.inf.gmanda.gui.dialogs;

import org.joda.time.DateTime;

public class ImportFromGmaneDialog extends javax.swing.JDialog {

	public enum FetchType {
		ALL, BYDATE, BYID;
	}

	/** A return status code - returned if Cancel button has been pressed */
	public static final int RET_CANCEL = 0;

	/** A return status code - returned if OK button has been pressed */
	public static final int RET_OK = 1;

	/** Creates new form GmandaImportDialog */
	public ImportFromGmaneDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	/** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
	public int getReturnStatus() {
		return returnStatus;
	}

	public String getListName() {
		return tListName.getText();
	}

	public FetchType getFetchType() {
		if (rFetchAll.isSelected()) {
			return FetchType.ALL;
		}
		if (rFetchByDate.isSelected()) {
			return FetchType.BYDATE;
		}
		if (rFetchById.isSelected()) {
			return FetchType.BYID;
		}

		assert false;

		return null;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		tListName = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		rFetchByDate = new javax.swing.JRadioButton();
		rFetchById = new javax.swing.JRadioButton();
		rFetchAll = new javax.swing.JRadioButton();
		jLabel5 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		tEndId = new javax.swing.JTextField();
		tStartId = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		tEndDate = new javax.swing.JTextField();
		tStartDate = new javax.swing.JTextField();
		cancelButton = new javax.swing.JButton();
		okButton = new javax.swing.JButton();

		setName("Form"); // NOI18N
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		tListName.setName("tListName"); // NOI18N

		jLabel1.setText("Name of the List to Fetch:");
		jLabel1.setName("jLabel1"); // NOI18N

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel1.setName("jPanel1"); // NOI18N

		buttonGroup1.add(rFetchByDate);
		rFetchByDate.setText("Fetch by Date Range");
		rFetchByDate.setName("jRadioButton1"); // NOI18N

		buttonGroup1.add(rFetchById);
		rFetchById.setText("Fetch by Id");
		rFetchById.setName("jRadioButton2"); // NOI18N

		buttonGroup1.add(rFetchAll);
		rFetchAll.setSelected(true);
		rFetchAll.setText("Fetch all available messages");
		rFetchAll.setName("jRadioButton3"); // NOI18N

		jLabel5.setText("End Id:");
		jLabel5.setName("jLabel5"); // NOI18N

		jLabel4.setText("Start Id:");
		jLabel4.setName("jLabel4"); // NOI18N

		tEndId.setName("tEndId"); // NOI18N

		tStartId.setName("fieldStartId"); // NOI18N

		jLabel3.setText("End Date:");
		jLabel3.setName("jLabel3"); // NOI18N

		jLabel2.setText("Start Date:");
		jLabel2.setName("jLabel2"); // NOI18N

		tEndDate.setName("tEndDate"); // NOI18N

		tStartDate.setName("tStartDate"); // NOI18N

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
						jPanel1Layout.createSequentialGroup().addComponent(rFetchByDate).addGap(
							237, 237, 237)).addGroup(
						jPanel1Layout.createSequentialGroup().addComponent(rFetchById).addGap(283,
							283, 283)).addGroup(
						jPanel1Layout.createSequentialGroup().addComponent(rFetchAll).addGap(203,
							203, 203)).addGroup(
						jPanel1Layout.createSequentialGroup().addGap(21, 21, 21).addGroup(
							jPanel1Layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.TRAILING).addGroup(
								javax.swing.GroupLayout.Alignment.LEADING,
								jPanel1Layout.createSequentialGroup().addGroup(
									jPanel1Layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING).addComponent(
										jLabel5).addComponent(jLabel4)).addGap(34, 34, 34)
									.addGroup(
										jPanel1Layout.createParallelGroup(
											javax.swing.GroupLayout.Alignment.LEADING)
											.addComponent(tEndId,
												javax.swing.GroupLayout.DEFAULT_SIZE, 437,
												Short.MAX_VALUE).addComponent(tStartId,
												javax.swing.GroupLayout.DEFAULT_SIZE, 437,
												Short.MAX_VALUE))).addGroup(
								javax.swing.GroupLayout.Alignment.LEADING,
								jPanel1Layout.createSequentialGroup().addGroup(
									jPanel1Layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING).addComponent(
										jLabel3).addComponent(jLabel2)).addGap(21, 21, 21)
									.addGroup(
										jPanel1Layout.createParallelGroup(
											javax.swing.GroupLayout.Alignment.LEADING)
											.addComponent(tEndDate,
												javax.swing.GroupLayout.DEFAULT_SIZE, 437,
												Short.MAX_VALUE).addComponent(tStartDate,
												javax.swing.GroupLayout.DEFAULT_SIZE, 437,
												Short.MAX_VALUE)))))).addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(rFetchAll)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
					rFetchByDate).addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel2).addComponent(tStartDate,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel3).addComponent(tEndDate,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rFetchById)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel4).addComponent(tStartId,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel5).addComponent(tEndId,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(
					javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		cancelButton.setText("Cancel");
		cancelButton.setName("cancelButton"); // NOI18N
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		okButton.setText("OK");
		okButton.setName("okButton"); // NOI18N
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			layout.createSequentialGroup().addGap(20, 20, 20).addComponent(tListName,
				javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE).addContainerGap())
			.addGroup(
				layout.createSequentialGroup().addContainerGap().addGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						layout.createSequentialGroup().addComponent(jPanel1,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap()).addGroup(
						layout.createSequentialGroup().addComponent(jLabel1,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(438, 438,
							438)))).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(397, Short.MAX_VALUE).addComponent(
					cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80,
					javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(okButton,
					javax.swing.GroupLayout.PREFERRED_SIZE, 80,
					javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			javax.swing.GroupLayout.Alignment.TRAILING,
			layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(
				javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tListName,
				javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(okButton).addComponent(cancelButton)).addGap(16, 16, 16)));

		pack();
	}// </editor-fold>

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		doClose(RET_OK);
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		doClose(RET_CANCEL);
	}

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		doClose(RET_CANCEL);
	}

	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();
	}

	// Variables declaration - do not modify
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JButton cancelButton;
	private javax.swing.JTextField tStartId;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JRadioButton rFetchByDate;
	private javax.swing.JRadioButton rFetchById;
	private javax.swing.JRadioButton rFetchAll;
	private javax.swing.JButton okButton;
	private javax.swing.JTextField tEndDate;
	private javax.swing.JTextField tEndId;
	private javax.swing.JTextField tListName;
	private javax.swing.JTextField tStartDate;
	// End of variables declaration

	private int returnStatus = RET_CANCEL;

	public DateTime getStartDate() {
		return new DateTime(tStartDate.getText());
	}

	public DateTime getEndDate() {
		return new DateTime(tEndDate.getText());
	}

	public int getStartId() {
		return Integer.parseInt(tStartId.getText());
	}

	public int getEndId() {
		return Integer.parseInt(tEndId.getText());
	}
}