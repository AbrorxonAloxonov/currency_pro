import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h2>Java Hipsterga, hush kelibsiz!</h2>
        <p className="lead">Bosh sahifa</p>
        {account?.login ? (
          <div>
            <Alert color="success">Siz &quot;{account.login}&quot; foydalanuvchisi sifatida kirdiz.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              Agar
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                avtarizatsiyadan o`tishni
              </Link>
              xohlasangiz, mavjud foydalanuvchini tekshirib ko`ring:
              <br />- Ma`mur (login=&quot;admin&quot; va parol=&quot;admin&quot;) <br />- Oddiy foydalanuvchi (login=&quot;user&quot; va
              parol=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              Siz ro`yhatdan o`tmaganmisiz?&nbsp;
              <Link to="/account/register" className="alert-link">
                Yangi foydalanuvchi qo`shing
              </Link>
            </Alert>
          </div>
        )}
        <p>JHipster bo`yicha savollaringiz bo`lsa:</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              JHipster bosh sahifasi
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              JHipster Stack Overflow
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              JHipster bag trekeri
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              JHipster umumiy chat xonasi
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              Twitterdagi logini @jhipster
            </a>
          </li>
        </ul>

        <p>
          Agar JHipster sizga yoqsa, ushbu manzilda yulduzcha quyishni ununmang{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;
