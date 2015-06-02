package org.edx.mobile.module.db;

import android.database.Cursor;

import org.edx.mobile.model.VideoModel;
import org.edx.mobile.model.api.VideoResponseModel;
import org.edx.mobile.model.course.BlockPath;
import org.edx.mobile.model.course.IBlock;
import org.edx.mobile.model.course.VideoBlockModel;
import org.edx.mobile.model.course.VideoData;
import org.edx.mobile.model.db.DownloadEntry;

/**
 * Model Factory class for the database models.
 * @author rohan
 *
 */
public class DatabaseModelFactory {

    /**
     * Returns new instance of {@link org.edx.mobile.model.VideoModel} initialized with given cursor.
     * @param c
     * @return
     */
    public static VideoModel getModel(Cursor c) {
        DownloadEntry de = new DownloadEntry();
        
        de.dmId = c.getLong(c.getColumnIndex(DbStructure.Column.DM_ID));
        de.downloaded = DownloadEntry.DownloadedState.values()[c.getInt(c.getColumnIndex(DbStructure.Column.DOWNLOADED))];
        de.duration = c.getLong(c.getColumnIndex(DbStructure.Column.DURATION));
        de.filepath = c.getString(c.getColumnIndex(DbStructure.Column.FILEPATH));
        de.id = c.getInt(c.getColumnIndex(DbStructure.Column.ID));
        de.size = c.getLong(c.getColumnIndex(DbStructure.Column.SIZE));
        de.username = c.getString(c.getColumnIndex(DbStructure.Column.USERNAME));
        de.title = c.getString(c.getColumnIndex(DbStructure.Column.TITLE));
        de.url = c.getString(c.getColumnIndex(DbStructure.Column.URL));
        de.url_high_quality = c.getString(c.getColumnIndex(DbStructure.Column.URL_HIGH_QUALITY));
        de.url_low_quality = c.getString(c.getColumnIndex(DbStructure.Column.URL_LOW_QUALITY));
        de.url_youtube = c.getString(c.getColumnIndex(DbStructure.Column.URL_YOUTUBE));
        de.videoId = c.getString(c.getColumnIndex(DbStructure.Column.VIDEO_ID));
        de.watched = DownloadEntry.WatchedState.values()[c.getInt(c.getColumnIndex(DbStructure.Column.WATCHED))];
        de.eid = c.getString(c.getColumnIndex(DbStructure.Column.EID));
        de.chapter = c.getString(c.getColumnIndex(DbStructure.Column.CHAPTER));
        de.section = c.getString(c.getColumnIndex(DbStructure.Column.SECTION));
        de.downloadedOn = c.getLong(c.getColumnIndex(DbStructure.Column.DOWNLOADED_ON));
        de.lastPlayedOffset = c.getInt(c.getColumnIndex(DbStructure.Column.LAST_PLAYED_OFFSET));
        de.isCourseActive = c.getInt(c.getColumnIndex(DbStructure.Column.IS_COURSE_ACTIVE));
        de.isVideoForWebOnly = c.getInt(c.getColumnIndex(DbStructure.Column.VIDEO_FOR_WEB_ONLY)) == 1;
        de.lmsUrl = c.getString(c.getColumnIndex(DbStructure.Column.UNIT_URL));
        
        return de;
    }

    /**
     * Returns an object of IVideoModel which has all the fields copied from given VideoResponseModel.
     * @param vrm
     * @return
     */
    public static VideoModel getModel(VideoResponseModel vrm) {
        DownloadEntry e = new DownloadEntry();
        e.chapter = vrm.getChapterName();
        e.section = vrm.getSequentialName();
        e.eid = vrm.getCourseId();
        e.duration = vrm.getSummary().getDuration();
        e.size = vrm.getSummary().getSize();
        e.title = vrm.getSummary().getDisplayName();
        e.url = vrm.getSummary().getVideoUrl();
        e.url_high_quality = vrm.getSummary().getHighEncoding();
        e.url_low_quality = vrm.getSummary().getLowEncoding();
        e.url_youtube = vrm.getSummary().getYoutubeLink();
        e.videoId = vrm.getSummary().getId();
        e.transcript = vrm.getSummary().getTranscripts();
        e.lmsUrl = vrm.getUnitUrl();

        e.isVideoForWebOnly = vrm.getSummary().isOnlyOnWeb();
        return e;
    }

    /**
     *  Returns an object of IVideoModel which has all the fields copied from given VideoData.
     * @param vrm
     * @return
     */
    public static VideoModel getModel(VideoData vrm, VideoBlockModel block) {
        DownloadEntry e = new DownloadEntry();
        //FIXME - current database schema is not suitable for arbitary level of course structure tree
        //solution - store the navigation path info in into one column field in the database,
        //rather than individual column fields.
        BlockPath path = block.getPath();
        e.chapter = path.get(1) == null ? "" : path.get(1).getDisplayName();
        e.section =  path.get(2) == null ? "" : path.get(2).getDisplayName();
        IBlock root = block.getRoot();
        e.eid = root.getId();
        e.duration = vrm.duration;
        e.size =  vrm.encodedVideos.getPreferredVideoInfo().fileSize;
        e.title = block.getDisplayName();
        e.url = vrm.encodedVideos.getPreferredVideoInfo().url;
        e.url_high_quality = vrm.encodedVideos.mobileHigh == null ? "" : vrm.encodedVideos.mobileHigh.url;
        e.url_low_quality = vrm.encodedVideos.mobileLow == null ? "": vrm.encodedVideos.mobileLow.url;
        e.url_youtube = vrm.encodedVideos.youtube == null ? "" : vrm.encodedVideos.youtube.url;
        e.videoId = block.getId();
        e.transcript = vrm.transcripts;
        e.lmsUrl = block.getBlockUrl();

        e.isVideoForWebOnly = vrm.onlyOnWeb;
        return e;
    }
}
