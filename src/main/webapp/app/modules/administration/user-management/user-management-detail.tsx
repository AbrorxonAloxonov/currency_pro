import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Badge } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';

import { getUser } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserManagementDetail = () => {
  const dispatch = useAppDispatch();

  const { login } = useParams<'login'>();

  useEffect(() => {
    dispatch(getUser(login));
  }, []);

  const user = useAppSelector(state => state.userManagement.user);

  return (
    <div>
      <h2>
        Foydalanuvchi [<strong>{user.login}</strong>]
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>Login</dt>
          <dd>
            <span>{user.login}</span>&nbsp;
            {user.activated ? <Badge color="success">Faol</Badge> : <Badge color="danger">Faol emas</Badge>}
          </dd>
          <dt>Ismi</dt>
          <dd>{user.firstName}</dd>
          <dt>Familiyasi</dt>
          <dd>{user.lastName}</dd>
          <dt>Email</dt>
          <dd>{user.email}</dd>
          <dt>Qo&apos;shgan</dt>
          <dd>{user.createdBy}</dd>
          <dt>Qo&apos;shilgan vaqti</dt>
          <dd>{user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}</dd>
          <dt>O&apos;zgartirgan</dt>
          <dd>{user.lastModifiedBy}</dd>
          <dt>O&apos;zgartirilgan sanasi</dt>
          <dd>
            {user.lastModifiedDate ? (
              <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
          <dt>Profili</dt>
          <dd>
            <ul className="list-unstyled">
              {user.authorities
                ? user.authorities.map((authority, i) => (
                    <li key={`user-auth-${i}`}>
                      <Badge color="info">{authority}</Badge>
                    </li>
                  ))
                : null}
            </ul>
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/admin/user-management" replace color="info">
        <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Orqaga</span>
      </Button>
    </div>
  );
};

export default UserManagementDetail;
