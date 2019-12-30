import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class InputItem {
	public final Metadata metadata;
	public final File     path;
	public boolean problem = false;

	public InputItem(File path) throws IOException, ImageProcessingException {
		this.path = path;
		this.metadata = ImageMetadataReader.readMetadata(path);
	}

	public Date getDateOriginal() {
		return metadata.containsDirectoryOfType(ExifSubIFDDirectory.class)
				? metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class).getDateOriginal()
				: null;
	}

	public Date getNewDate(Date newDate, long timeOffset) {
		Date date = getDateOriginal();
		if (date == null)
			date = new Date();
		if (newDate != null) {
			Calendar newC = Calendar.getInstance();
			newC.setTime(date);
			newC.set(Calendar.HOUR_OF_DAY, date.getHours());
			newC.set(Calendar.MINUTE, date.getMinutes());
			newC.set(Calendar.SECOND, date.getSeconds());
			newC.set(Calendar.MILLISECOND, 0);
			date = newC.getTime();
		}
		if (timeOffset != 0)
			date = new Date(date.getTime() + timeOffset);
		return date;
	}

	public void adjust(File outPath, Date newDate) throws IOException, ImageReadException, ImageWriteException {
		try (FileOutputStream fos = new FileOutputStream(outPath); OutputStream os = new BufferedOutputStream(fos)) {
			TiffOutputSet           outputSet    = null;
			final ImageMetadata     metadata     = Imaging.getMetadata(path);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			if (jpegMetadata != null) {
				final TiffImageMetadata exif = jpegMetadata.getExif();
				if (exif != null) {
					outputSet = exif.getOutputSet();
				}
			}
			if (outputSet == null) {
				outputSet = new TiffOutputSet();
			}
			{
				final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
				exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				exifDirectory.add(
						ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL,
						new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(newDate)
				);
			}

			new ExifRewriter().updateExifMetadataLossless(path, os, outputSet);
		}
	}

	@Override
	public String toString() {
		return path.getName();
	}
}
