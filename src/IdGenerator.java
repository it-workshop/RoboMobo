/**
 * Created by cra on 12/9/13.
 * Dummy ID generator. NOT thread-safe
 */
public class IdGenerator
{
    private static IdGenerator m_instance = null;
    private int m_currentPlayerId;

    protected IdGenerator()
    {
        m_currentPlayerId = 0;
        // should never be initialized
    }

    public int generatePlayerId()
    {
        m_currentPlayerId++;
        return m_currentPlayerId;
    }

    public static IdGenerator getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new IdGenerator();
        }
        return m_instance;
    }
}
