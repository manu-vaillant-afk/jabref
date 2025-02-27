package org.jabref.gui.dialogs;

import java.nio.file.Path;

import org.jabref.gui.DialogService;
import org.jabref.gui.util.DefaultTaskExecutor;
import org.jabref.logic.autosaveandbackup.BackupManager;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.util.BackupFileType;
import org.jabref.logic.util.io.BackupFileUtil;

/**
 * Stores all user dialogs related to {@link BackupManager}.
 */
public class BackupUIManager {

    private BackupUIManager() {
    }

    public static void showRestoreBackupDialog(DialogService dialogService, Path originalPath) {
        String content = new StringBuilder()
                .append(Localization.lang("A backup file for '%0' was found at '%1'.",
                        originalPath.getFileName().toString(),
                        // We need to determine the path "manually" as the path does not get passed through when a diff is detected.
                        BackupFileUtil.getPathOfLatestExisingBackupFile(originalPath, BackupFileType.BACKUP).map(Path::toString).orElse(Localization.lang("File not found"))))
                .append("\n")
                .append(Localization.lang("This could indicate that JabRef did not shut down cleanly last time the file was used."))
                .append("\n\n")
                .append(Localization.lang("Do you want to recover the library from the backup file?"))
                .toString();

        boolean restoreClicked = DefaultTaskExecutor.runInJavaFXThread(() -> dialogService.showConfirmationDialogAndWait(
                Localization.lang("Backup found"), content,
                Localization.lang("Restore from backup"),
                Localization.lang("Ignore backup")));

        if (restoreClicked) {
            BackupManager.restoreBackup(originalPath);
        }
    }
}
