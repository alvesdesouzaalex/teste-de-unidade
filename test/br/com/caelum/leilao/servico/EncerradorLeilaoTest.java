package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;

public class EncerradorLeilaoTest {

	@Test
	public void deveEncerrarLeloisQueComecaramUmaSemanaAnte() {

		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 11, 9);

		Leilao leilao1 = new CriadorDeLeilao().para("TV LED 42 PL").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("GELADEIRA FROST FREE").naData(antiga).constroi();
		List<Leilao> antigos = Arrays.asList(leilao1, leilao2);

		LeilaoDao daoFalso = mock(LeilaoDao.class);
//		when(daoFalso.correntes()).thenReturn(antigos);
		when(daoFalso.correntes()).thenReturn(antigos);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();

		assertEquals(2, encerrador.getTotalEncerrados());

		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());

	}

	@Test
	public void naoDeveEncerrarLeiloesQueComecaramOntem() {

		Calendar ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);

		Leilao leilao1 = new CriadorDeLeilao().para("ULTRABOOK DELL 16GB").naData(ontem).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("IPHONE X").naData(ontem).constroi();
		Leilao leilao3 = new CriadorDeLeilao().para("SMARTWHATCH ANDROID OREON").naData(ontem).constroi();

		List<Leilao> antigos = Arrays.asList(leilao1, leilao2, leilao3);

		LeilaoDao daoFalso = mock(LeilaoDao.class);
		when(daoFalso.correntes()).thenReturn(antigos);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());

		assertFalse(leilao1.isEncerrado());
		assertFalse(leilao2.isEncerrado());
		assertFalse(leilao3.isEncerrado());

	}

	@Test
	public void naoEncontrouNenhumLeilaoEntaoNaoFazNada() {

		LeilaoDao daoFalso = mock(LeilaoDao.class);
		when(daoFalso.correntes()).thenReturn(new ArrayList<>());

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());
	}
	
	@Test
	public void testEsquecendoWhen() {

		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 11, 9);

		Leilao leilao1 = new CriadorDeLeilao().para("TV LED 42 PL").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("GELADEIRA FROST FREE").naData(antiga).constroi();
		List<Leilao> antigos = Arrays.asList(leilao1, leilao2);

		LeilaoDao daoFalso = mock(LeilaoDao.class);
//		when(daoFalso.correntes()).thenReturn(antigos);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();

		assertEquals(2, encerrador.getTotalEncerrados());

		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());

	}
	
	/**
	 * O Mockito não aceita mocar métodos statics
	 */
	
//	@Test
//	public void testMetodosStaticsMock() {
//		String daoFalso = mock(LeilaoDao.teste();
//	}

}
