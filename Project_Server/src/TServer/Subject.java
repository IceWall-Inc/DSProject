package TServer;

public interface Subject {
	public void registerObserver(Observer o);

	public void removeObserver(Observer o);

	public void notifyAllObservers(String message);
}
// INTERFACE E NEVOJSHME PER TE DEFINUAR FUNKSIONET E OBSERVERS