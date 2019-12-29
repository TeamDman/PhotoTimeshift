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
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;
import java.util.Date;

public class InputItem {
	public final Metadata metadata;
	public final File     path;

	public InputItem(File path) throws IOException, ImageProcessingException {
		this.path = path;
		this.metadata = ImageMetadataReader.readMetadata(path);
	}

	public Date getDateOriginal() {
		return metadata.containsDirectoryOfType(ExifSubIFDDirectory.class)
				? metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class).getDateOriginal()
				: null;
	}

	private static void adjust(File inPath, File outPath) throws IOException, ImageReadException, ImageWriteException {
		try (FileOutputStream fos = new FileOutputStream(outPath); OutputStream os = new BufferedOutputStream(fos)) {
			TiffOutputSet           outputSet    = null;
			final ImageMetadata     metadata     = Imaging.getMetadata(inPath);
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

			new ExifRewriter().updateExifMetadataLossless(inPath, os, outputSet);
		}
	}

	@Override
	public String toString() {
		return path.getName();
	}
}
