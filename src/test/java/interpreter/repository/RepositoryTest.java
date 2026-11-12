package interpreter.repository;

import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.NopStmt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    private PrgState state1;
    private PrgState state2;

    @BeforeEach
    void setUp() {
        state1 = PrgState.of(new NopStmt());
        state2 = PrgState.of(new NopStmt());
    }

    @Test
    void getCrtPrgReturnsInitialProgramState() {
        IRepository repo = new Repository(state1);
        assertSame(state1, repo.getCrtPrg());
    }

    @Test
    void emptyRepositoryReturnsNullForCrtPrg() {
        IRepository repo = new Repository();
        assertNull(repo.getCrtPrg());
        assertEquals(0, repo.getStates().size());
    }

    @Test
    void addProgramStateAppendsToList() {
        IRepository repo = new Repository();
        repo.add(state1);
        assertSame(state1, repo.getCrtPrg());
        assertEquals(1, repo.getStates().size());

        repo.add(state2);
        assertEquals(2, repo.getStates().size());
        assertSame(state1, repo.getCrtPrg());
    }

    @Test
    void logFilePathConstructorsWork() {
        Repository repoWithLog = new Repository(state1, "log.txt");
        assertEquals("log.txt", repoWithLog.getLogFilePath());
        assertSame(state1, repoWithLog.getCrtPrg());

        Repository repoPathOnly = new Repository("log2.txt");
        assertEquals("log2.txt", repoPathOnly.getLogFilePath());
        assertNull(repoPathOnly.getCrtPrg());
    }
}

