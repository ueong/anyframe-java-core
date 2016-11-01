/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anyframe.util.system;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anyframe.util.DateUtil;
import org.anyframe.util.system.sigar.SigarAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.FileWatcher;
import org.hyperic.sigar.FileWatcherThread;
import org.hyperic.sigar.SigarException;
import org.springframework.util.ReflectionUtils;

/**
 * As a directory-monitoring utility class, it can log the information of
 * add/modify/delete file in the monitoring-target directory. Multiple files in
 * the directory can cause lower performance, and problem may occur if Korean
 * file name exists in the monitoring-target directory for Windows.
 * <p>
 * The following is an example of file logging from directory monitoring start ~
 * stop at every five seconds of monitoring. added/modified/deleted contents of
 * file are logged in prefix(NEW/MOD/DEL) or file name, and the modified size is
 * shown in before|after format.
 *
 * <pre>
 * 2011-01-19 10:13:01,625  INFO [anyframe.core.util.system.FileMonitor] FileWatcherThread started.
 * 2011-01-19 10:13:06,718  INFO [anyframe.core.util.system.FileMonitor] NEW$D:\workspace_neis_helios\local.common-component.system\test\default2.txt${Mtime: 1월 19 10:13}{Size: 52}
 * 2011-01-19 10:13:11,750  INFO [anyframe.core.util.system.FileMonitor] NEW$D:\workspace_neis_helios\local.common-component.system\test\default3.txt${Mtime: 1월 19 10:13}{Size: 52}
 * 2011-01-19 10:13:11,765  INFO [anyframe.core.util.system.FileMonitor] NEW$D:\workspace_neis_helios\local.common-component.system\test\default4.txt${Mtime: 1월 19 10:13}{Size: 16}
 * 2011-01-19 10:13:11,765  INFO [anyframe.core.util.system.FileMonitor] NEW$D:\workspace_neis_helios\local.common-component.system\test\default5.txt${Mtime: 1월 19 10:13}{Size: 16}
 * 2011-01-19 10:13:11,812  INFO [anyframe.core.util.system.FileMonitor] MOD$D:\workspace_neis_helios\local.common-component.system\test\default2.txt${Mtime: 1월 19 10:13|1월 19 10:13}{Size: 52|16}
 * 2011-01-19 10:13:16,828  INFO [anyframe.core.util.system.FileMonitor] DEL$D:\workspace_neis_helios\local.common-component.system\test\default5.txt
 * 2011-01-19 10:13:16,843  INFO [anyframe.core.util.system.FileMonitor] MOD$D:\workspace_neis_helios\local.common-component.system\test\default4.txt${Mtime: 1월 19 10:13|1월 19 10:13}{Size: 16|24}
 * 2011-01-19 10:13:16,859  INFO [anyframe.core.util.system.FileMonitor] DEL$D:\workspace_neis_helios\local.common-component.system\test\default3.txt
 * 2011-01-19 10:13:21,625  INFO [anyframe.core.util.system.FileMonitor] FileWatcherThread stopped.
 *
 * </pre>
 *
 * @author ByungHun Woo
 *
 */
public abstract class FileMonitor extends SystemUtilBase {

	/**
	 * default FileMonitor Logger
	 */
	private static final Log log = LogFactory.getLog(FileMonitor.class);

	private static boolean running = false;

	/**
	 * start directory monitoring. For easier start and end of monitoring, use
	 * Singleton monitoring thread. monitor target directory using
	 * FileWatcherThread of Sigar. set as monitoring-target default FileMonitor
	 * Logger and FileWatcherThread default interval.
	 *
	 * @param targetDir
	 * @return
	 * @see org.hyperic.sigar.FileWatcherThread
	 */
	public static boolean startSingleton(String targetDir) {
		return startSingleton(targetDir, FileWatcherThread.DEFAULT_INTERVAL);
	}

	/**
	 * start directory monitoring. In this case, use Singleton monitoring thread
	 * by setting monitoring interval.
	 * @param targetDir
	 * @param interval - millisecond (ex. 300000 = 300 millisecond)
	 * @return
	 */
	public static boolean startSingleton(String targetDir, long interval) {
		return startSingleton(log, targetDir, interval);
	}

	/**
	 * start directory monitoring. Use Singleton monitoring thread by setting
	 * Logger and monitoring interval. The following is an example of setting
	 * log file(dailyRollingFile) and user-defied Logger name.
	 * <p>
	 *
	 * <pre>
	 * 		<appender name="fileMonitor"
	 * 			class="org.apache.log4j.DailyRollingFileAppender">
	 * 			<param name="File"
	 * 				value="logs/fileMonitor.log" />
	 * 			<param name="Append" value="true" />
	 * 			<param name="DatePattern" value="'.'yyyy-MM-dd" />
	 * 			<layout class="org.apache.log4j.PatternLayout">
	 * 				<param name="ConversionPattern"
	 * 					value="%d %5p [%c] %m%n" />
	 * 			</layout>
	 * 		</appender>
	 * 		..
	 * 		<logger name="fileMonitorLogger" additivity="false">
	 *         	<level value="INFO"/>
	 *         	<appender-ref ref="fileMonitor"/>
	 *     	</logger>
	 * </pre>
	 * @param log
	 * @param targetDir
	 * @param interval
	 * @return
	 */
	public static boolean startSingleton(Log log, String targetDir, long interval) {
		if (isRunning()) {
			log.error("Singleton FileWatcherThread already running!");
			return false;
		}

		forceReviveSingletonFileWatcherThread();

		synchronized (targetDir) {
			if (start(log, FileWatcherThread.getInstance(), targetDir, interval)) {
				setRunning(true);
				return true;
			}
			else {
				return false;
			}
		}
	}

	/**
	 * Start directory monitoring. In this case, by creating separate monitoring
	 * thread, it is possible to log information on add/modify/delete file in
	 * the monitoring-target directory. If there are multiple monitoring-target
	 * directory, keep returned FileWatcherThread and transferred Logger then
	 * handle them through the FileWatcherThread at the end. Note that for more
	 * clear analysis of multiple directory monitoring function, set Logger (log
	 * file) and monitoring interval separately.
	 * @param log
	 * @param targetDir
	 * @param interval
	 * @return
	 */
	public static FileWatcherThread start(Log log, String targetDir, long interval) {
		FileWatcherThread watcherThread = new FileWatcherThread();

		start(log, watcherThread, targetDir, interval);

		return watcherThread;
	}

	/**
	 * Start directory monitoring. get Logger and monitoring thread and log the
	 * information on add/modify/delete file in the monitoring target directory.
	 * FileWatcher of Sigar was extended and implemented.
	 * @param log
	 * @param watcherThread
	 * @param targetDir
	 * @param interval
	 * @return
	 * @see org.hyperic.sigar.FileWatcherThread
	 * @see org.hyperic.sigar.FileWatcher
	 */
	protected static boolean start(final Log log, final FileWatcherThread watcherThread, final String targetDir,
			final long interval) {

		return processIO(new IOCallback<Boolean>() {

			/**
			 * default repository to compare and check added file
			 */
			private Map<File, Long> baseFiles = new HashMap<File, Long>();

			public Boolean doInProcessIO() throws IOException, SigarException {
				final File targetDirFile = new File(targetDir);
				if (!targetDirFile.exists() || !targetDirFile.canRead()) {
					log.error("targetDir does not exists or can't read : " + targetDir);
					return false;
				}

				// interval
				watcherThread.setInterval(interval);

				FileWatcher watcher = new FileWatcher(SigarAccessor.getInstance().getSigar()) {
					// temporary repository to remove deleted file from
					// monitoring-target repository
					private List<String> toRemoveList = new ArrayList<String>();

					/**
					 * modified
					 */
					public void onChange(FileInfo info) {
						log.info("MOD$" + info.getName() + "$" + info.diff());
					}

					/**
					 * deleted
					 */
					public void onNotFound(FileInfo info) {
						log.info("DEL$" + info.getName());
						// Note that immediate removal can cause
						// java.util.ConcurrentModificationException
						// due to simultaneity issue
						// remove(info.getName());
						// save the deleted file to remove list temporary
						toRemoveList.add(info.getName());
					}

					public void onException(FileInfo info, SigarException e) {
						// log.info("Error checking " + info.getName() + ":");
						// e.printStackTrace();
						log.error("Error checking " + info.getName() + ":", e);
					}

					@Override
					/**
					 * monitor and log add/modify/delete file at every monitoring.
					 * In this case, additional logic for file creating and removing monitoring target has been extended and implemented.
					 */
					public void check() {
						addWatcherCreatedFile();
						super.check();
						removeDeletedFiles();
					}

					/**
					 * In case of modification of the Collection object during
					 * iterator handling for Collections.synchronizedSet in
					 * check of super, java.util.ConcurrentModificationException
					 * occurs. Therefore, keep the removal target and handle
					 * here.
					 */
					private void removeDeletedFiles() {
						for (String deletedFile : toRemoveList) {
							remove(deletedFile);
						}
						toRemoveList.clear();
					}

					/**
					 * monitor file list in directory at every monitoring, and
					 * log additionally if there is any file not existing (new
					 * file) in default repository. Note that multiple files in
					 * directory can cause lower performance.
					 */
					private void addWatcherCreatedFile() {
						for (File file : targetDirFile.listFiles()) {
							if (baseFiles.get(file) == null) {
								baseFiles.put(file, file.lastModified());
								try {
									log.info("NEW$" + file.getAbsolutePath() + "${Mtime: "
											+ DateUtil.date2String(new Date(file.lastModified()), "MMM dd HH:mm")
											+ "}{Size: " + file.length() + "}");
									add(file);
								}
								catch (Exception e) {
									log.error("sigar FileWatcher add failed. " + file.getAbsolutePath(), e);
								}
							}
						}
					}

				};
				watcher.setInterval(watcherThread.getInterval());

				// do not monitor targetDir itself.
				// as modification of subfile leads to update of lastModifed
				// everytime, too many logs will be made.
				// watcher.add(targetDirFile);

				if (targetDirFile.isDirectory()) {
					File[] orgFiles = targetDirFile.listFiles();
					// org baseFiles
					for (File file : orgFiles) {
						baseFiles.put(file, file.lastModified());
					}
					// if Korean file name exists for Windows, exception occurs
					// in JNI!
					watcher.add(orgFiles);
				}

				log.info("FileWatcherThread started.");
				watcherThread.add(watcher);
				watcherThread.doStart();

				return true;
			}

		});

	}

	/**
	 * stop directory monitoring. use Singleton monitoring thread.
	 */
	public static void stopSingleton() {
		stopSingleton(log);
	}

	/**
	 * stop directory monitoring. set Logger transferred and kept at start and
	 * use Singleton monitoring thread.
	 * @param log
	 */
	public static void stopSingleton(Log log) {
		FileWatcherThread watcherThread = FileWatcherThread.getInstance();
		watcherThread.doStop();
		setRunning(false);
		log.info("FileWatcherThread stopped.");
	}

	/**
	 * stop directory monitoring. Individually executed using separate
	 * monitoring thread and make sure set thread and Logger used at start.
	 * @param log
	 * @param watcherThread
	 */
	public static void stop(Log log, FileWatcherThread watcherThread) {
		watcherThread.doStop();
		log.info("FileWatcherThread stopped.");
	}

	private static void setRunning(boolean running) {
		FileMonitor.running = running;
	}

	/**
	 * whether Singleton FileWatcherThread is currently running
	 * @return
	 */
	public static boolean isRunning() {
		return running;
	}

	/**
	 * In using FileWatcherThread of Sigar with Singleton, at the first doStop
	 * shouldDie attribute is defined true, therefore even then Thread is run
	 * later, chick logic cannot perform. Even in Singleton, in the case of
	 * start re-executed after thread was stopped, it is needed to forcefully
	 * change shouldDie attribute to false. For changing private filed, forcefully
	 * reset using Reflection function.
	 */
	public static void forceReviveSingletonFileWatcherThread() {
		processIO(new IOCallback<Object>() {
			public Object doInProcessIO() throws Exception {
				Field field = ReflectionUtils.findField(FileWatcherThread.class, "shouldDie");
				ReflectionUtils.makeAccessible(field);
				field.setBoolean(FileWatcherThread.getInstance(), false);
				return null;
			}
		});
	}

}