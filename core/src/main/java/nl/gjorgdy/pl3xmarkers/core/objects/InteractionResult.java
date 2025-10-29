package nl.gjorgdy.pl3xmarkers.core.objects;

public record InteractionResult(State state, String message) {

	public enum State {
		ADDED,
		REMOVED,
		FAILURE,
		SKIP
	}

	public static InteractionResult skip() {
		return new InteractionResult(State.SKIP, "");
	}

}
