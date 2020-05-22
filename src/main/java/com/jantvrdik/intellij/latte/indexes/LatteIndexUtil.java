package com.jantvrdik.intellij.latte.indexes;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.FileContentUtil;
import com.jantvrdik.intellij.latte.LatteFileType;
import com.jantvrdik.intellij.latte.config.LatteFileConfiguration;
import com.jantvrdik.intellij.latte.utils.LatteIdeHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LatteIndexUtil {

    final private static int WAIT_MILIS_BETWEEN_NOTIFY = 15000;

    public static void notifyRemovedFiles(List<Project> projects) {
        LatteIdeHelper.doNotify(
                "Latte plugin settings",
                "File latte-intellij.xml was removed. You can refresh configurations.",
                NotificationType.INFORMATION,
                null,
                new NotificationAction("Refresh Configuration") {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
                        for (Project project : projects) {
                            if (ActionUtil.isDumbMode(project)) {
                                showWaring(project);
                                return;
                            }
                        }

                        List<String> paths = new ArrayList<>();
                        for (Project project : projects) {
                            if (!reinitialize(project)) {
                                return;
                            }
                        }

                        notification.expire();

                        LatteIdeHelper.doNotify(
                                "Latte plugin settings",
                                "Configuration was loaded from files: " + String.join(", ", paths),
                                NotificationType.INFORMATION,
                                null
                        );
                    }
                }
        );
    }

    public static Notification notifyReparseFiles(Project project) {
        return LatteIdeHelper.doNotify(
                "Latte configuration reloaded",
                "Latte plugin detected latte-intellij.xml file. Latte files need reparse.",
                NotificationType.WARNING,
                project,
                true,
                new NotificationAction("Reparse Latte Files") {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification current) {
                        if (!reinitialize(project)) {
                            return;
                        }

                        current.expire();

                        LatteIdeHelper.doNotify(
                                "Latte plugin settings",
                                "Latte files was reparsed",
                                NotificationType.INFORMATION,
                                project
                        );
                    }
                }
        );
    }

    public static boolean reinitialize(Project project) {
        if (ActionUtil.isDumbMode(project)) {
            showWaring(project);
            return false;
        }

        LatteFileConfiguration.getInstance(project).reinitialize();

        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(LatteFileType.INSTANCE, GlobalSearchScope.allScope(project));
        FileContentUtil.reparseFiles(virtualFiles);
        return true;
    }

    public static boolean isNotificationOutdated(Notification notification) {
        return (notification.getTimestamp() + WAIT_MILIS_BETWEEN_NOTIFY) <= System.currentTimeMillis();
    }

    private static void showWaring(Project project) {
        LatteIdeHelper.doNotify(
                "Latte plugin warning",
                "Latte files can not be reparsed during indexing. Wait after indexing will be done.",
                NotificationType.ERROR,
                project
        );
    }
}