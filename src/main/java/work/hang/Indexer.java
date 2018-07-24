package work.hang;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * [概 要]
 * [环 境] JAVA 1.8
 *
 * @author 六哥
 * @date 2018/7/23
 */
public class Indexer {
	private Integer[] ids = {1, 2, 3};
	private String[] citys = {"aingdao", "banjing", "changhai"};
	private String[] descs = {
			"Qingdao is b beautiful city.",
			"Nanjing is c city of culture.",
			"Shanghai is d bustling city."
	};

	private Directory dir;

	/**
	 * 获取IndexWriter实例
	 *
	 * @return IndexWriter
	 * @throws Exception Exception
	 */
	private IndexWriter getWriter() throws Exception {
		// 标准分词器
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		return new IndexWriter(dir, iwc);
	}

	/**
	 * 生成索引
	 *
	 * @param indexDir indexDir
	 * @throws Exception Exception
	 */
	private void index(String indexDir) throws Exception {
		dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer = getWriter();
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			//FieldIdentifier fieldIdentifier = new Fil
			doc.add(new NumericDocValuesField("id",ids[i]));
			doc.add(new StringField("city", citys[i], Field.Store.YES));
			doc.add(new TextField("desc", descs[i], Field.Store.YES));
			// 添加文档
			writer.addDocument(doc);
		}
		writer.close();
	}


	public static void main(String[] args) throws Exception {
		new Indexer().index("/Users/maxiaohu/Desktop/lucene/lucene05");
	}
}
