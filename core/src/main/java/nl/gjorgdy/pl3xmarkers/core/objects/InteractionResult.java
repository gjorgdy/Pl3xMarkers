package nl.gjorgdy.pl3xmarkers.core.objects;

public record InteractionResult(State state, String message) {

	public static InteractionResult skip() {
		return new InteractionResult(State.SKIP, "");
	}

	public static InteractionResult added(String message) {
		return new InteractionResult(State.ADDED, message);
	}

	public static InteractionResult removed(String message) {
		return new InteractionResult(State.REMOVED, message);
	}

	public static InteractionResult failure(String message) {
		return new InteractionResult(State.FAILURE, message);
	}

	public static InteractionResult failure() {
		return new InteractionResult(State.FAILURE, "");
	}

	public enum State {
		ADDED,
		REMOVED,
		FAILURE,
		SKIP
	}

}
