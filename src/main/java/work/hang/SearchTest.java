package work.hang;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * [概 要]
 * [环 境] JAVA 1.8
 *
 * @author 六哥
 * @date 2018/7/23
 */
public class SearchTest {
	private IndexReader reader;
	private IndexSearcher is;

	@Before
	public void setUp() throws Exception {
		Directory dir = FSDirectory.open(Paths.get("/Users/maxiaohu/Desktop/lucene/lucene05"));
		reader = DirectoryReader.open(dir);
		is = new IndexSearcher(reader);
	}

	@After
	public void tearDown() throws Exception {
		reader.close();
	}

	private void get(TopDocs hits) throws Exception {
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}
	}

	/**
	 * 指定项范围搜索
	 *
	 * @throws Exception Exception
	 */
	@Test
	public void testTermRangeQuery() throws Exception {
		TermRangeQuery query = new TermRangeQuery("desc", new BytesRef("b".getBytes()), new BytesRef("c".getBytes()), true, true);
		TopDocs hits = is.search(query, 10);
		get(hits);
	}

	/**
	 * 指定数字范围
	 *
	 * @throws Exception Exception
	 */
	@Test
	public void testNumericRangeQuery() throws Exception {
		Query query = IntPoint.newRangeQuery("id", 1, 3);
		TopDocs hits = is.search(query, 10);
		get(hits);
	}

	/**
	 * 指定字符串开头搜索
	 *
	 * @throws Exception
	 */
	@Test
	public void testPrefixQuery() throws Exception {
		PrefixQuery query = new PrefixQuery(new Term("city", "a"));
		TopDocs hits = is.search(query, 10);
		get(hits);
	}

	/**
	 * 多条件查询
	 *
	 * @throws Exception
	 */
	@Test
	public void testBooleanQuery() throws Exception {
		Query query1 = IntPoint.newRangeQuery("id", 1, 3);
		PrefixQuery query2 = new PrefixQuery(new Term("city", "a"));
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(query1, BooleanClause.Occur.MUST);
		booleanQuery.add(query2, BooleanClause.Occur.MUST);
		TopDocs hits = is.search(booleanQuery.build(), 10);
		get(hits);
	}
}
