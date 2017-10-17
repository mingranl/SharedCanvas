package whiteboard;

//graphics interface
public interface Graph {
	public static final int TYPE_FREE = 0;
	public static final int TYPE_LINE = 1;
	public static final int TYPE_CIRCLE = 2;
	public static final int TYPE_OVAL = 3;
	public static final int TYPE_RECT = 4;
	public static final int TYPE_ERASER = 5;
	public static final int TYPE_IMAGE=6;

	int getType();
}
