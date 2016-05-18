package OldIMAP;
import java.util.ArrayList;

public class MessageHeader {

	private String mDate, mFrom, mSubject, mTo, mCc, mMessageId, mMime, mContentType;

	public MessageHeader(String mDate, String mFrom, String mSubject, String mTo, String mCc, String mMessageId,
			String mMime, String mContentType) {
		this.mDate = mDate;
		this.mFrom = mFrom;
		this.mSubject = mSubject;
		this.mTo = mTo;
		this.mCc = mCc;
		this.mMessageId = mMessageId;
		this.mMime = mMime;
		this.mContentType = mContentType;
	}

	public ArrayList<String> getHeaderStructure() {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(mDate);
		temp.add(mFrom);
		temp.add(mSubject);
		temp.add(mTo);
		temp.add(mCc);
		temp.add(mMessageId);
		temp.add(mMime);
		temp.add(mContentType);

		return temp;

	}

}
